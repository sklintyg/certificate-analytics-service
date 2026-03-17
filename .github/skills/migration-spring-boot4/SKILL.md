---
name: migration-spring-boot4
description: Upgrade a Spring Boot application from Spring Boot 3.5.x to Spring Boot 4.x. Handles Jackson 3 namespace migration, starter dependency restructuring, TestRestTemplate relocation, Spring Framework 7.x changes, and configuration property migrations. Validates with build and tests after changes.
license: MIT
---

# Spring Boot 4 Migration Skill

Use this skill to **upgrade a Spring Boot application from Spring Boot 3.5.x to Spring Boot 4.x**.

## Prerequisites

Before running this skill, ensure:
1. The **migration-analyze** skill has been run and any blocking issues have been addressed
2. **Java 25** migration is complete (run `migration-java25` skill first)
3. **Gradle 9** migration is complete (run `migration-gradle9` skill first)
4. The **intyg BOM** has been updated with Spring Boot 4.x and compatible 3pp versions
5. The `intygBomVersion` in `gradle.properties` points to the updated BOM

If prerequisites are not met, report what's missing and stop.

**Important context:** This is the most involved migration step. The biggest changes are:
- Jackson 3 namespace migration (`com.fasterxml.jackson` → `tools.jackson`)
- Starter dependency restructuring
- TestRestTemplate package relocation

## Migration procedure

### Step 1: Update BOM version for Spring Boot 4

1. Check `gradle.properties` → current `intygBomVersion`
2. Update `intygBomVersion` to the version that provides Spring Boot 4.x
3. Verify the BOM provides Spring Boot 4.x (the version catalog should include Boot 4 plugins)
4. If BOM is not updated, report and stop

### Step 2: Migrate Jackson 2 to Jackson 3 (CRITICAL)

Spring Boot 4 ships Jackson 3, which uses a completely different Java package namespace.
This is typically the largest change in the migration.

#### 2a: Update all Jackson imports

Replace all Jackson imports across the entire codebase:
```java
// BEFORE (Jackson 2):
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// AFTER (Jackson 3):
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonPOJOBuilder;
import tools.jackson.databind.json.JsonMapper;
```

The general pattern: `com.fasterxml.jackson.*` → `tools.jackson.*`

#### 2b: Remove JavaTimeModule registration

Jackson 3 has built-in Java time support. Remove all manual module registration:
```java
// REMOVE this pattern entirely:
ObjectMapper om = new ObjectMapper();
om.registerModule(new JavaTimeModule());
om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

// REPLACE with:
ObjectMapper om = JsonMapper.builder().build();
```

#### 2c: Update exception handling — Jackson 3 exceptions are UNCHECKED

In Jackson 3, `JacksonException` extends `RuntimeException` (not `IOException`). This means:
- Remove `try/catch` blocks that wrapped `JsonProcessingException` in `UncheckedIOException`
- Remove `throws JsonProcessingException` declarations
- Remove `UncheckedIOException` wrappers
- If you need to catch Jackson errors, catch `JacksonException` (unchecked)

```java
// BEFORE (Jackson 2 — checked exception):
public MyDto parse(String json) {
    try {
        return objectMapper.readValue(json, MyDto.class);
    } catch (JsonProcessingException e) {
        throw new UncheckedIOException(e);
    }
}

// AFTER (Jackson 3 — unchecked exception):
public MyDto parse(String json) {
    return objectMapper.readValue(json, MyDto.class);
}
```

In tests, update exception assertions:
```java
// BEFORE:
assertThrows(UncheckedIOException.class, () -> parser.parse(json));
when(objectMapper.readValue(json, MyDto.class)).thenThrow(JsonProcessingException.class);

// AFTER:
assertThrows(JacksonException.class, () -> parser.parse(json));
when(objectMapper.readValue(json, MyDto.class)).thenThrow(JacksonException.class);
```

#### 2d: Fix Lombok @Value/@Builder classes with Jackson deserialization

Lombok `@Value @Builder` classes that need Jackson deserialization require explicit annotations
for Jackson 3. The `@JsonDeserialize` and `@JsonPOJOBuilder` annotations must use the Jackson 3
package, and an explicit empty builder inner class must be declared:

```java
// BEFORE (Jackson 2 — implicit builder support):
@Value
@Builder
@JsonDeserialize(builder = MyDto.MyDtoBuilder.class)
public class MyDto {
    String name;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MyDtoBuilder {}
}

// AFTER (Jackson 3 — same structure, different imports):
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonPOJOBuilder;

@Value
@Builder
@JsonDeserialize(builder = MyDto.MyDtoBuilder.class)
public class MyDto {
    String name;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MyDtoBuilder {}
}
```

**Important:** If a `@Value @Builder` class did NOT previously have `@JsonDeserialize` and was
relying on Jackson 2's implicit handling, you MUST add the annotations and the empty builder class
for Jackson 3.

### Step 3: Update dependency starters

Spring Boot 4 restructured its modules. Scan all `build.gradle` files and apply these changes:

#### Jackson dependencies → spring-boot-starter-json
```groovy
// REMOVE (Boot 3.x — explicit Jackson deps):
implementation 'com.fasterxml.jackson.core:jackson-annotations'
implementation 'com.fasterxml.jackson.core:jackson-databind'
implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310'

// REPLACE with (Boot 4.x):
implementation 'org.springframework.boot:spring-boot-starter-json'
```

For test fixtures that need Jackson:
```groovy
// REMOVE:
testFixturesImplementation 'com.fasterxml.jackson.core:jackson-databind'
testFixturesImplementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310'

// REPLACE with:
testFixturesApi 'org.springframework.boot:spring-boot-starter-json'
```

#### Liquibase
```groovy
// BEFORE (Boot 3.x — direct dependency):
implementation 'org.liquibase:liquibase-core'

// AFTER (Boot 4.x — use starter):
implementation 'org.springframework.boot:spring-boot-starter-liquibase'
```

#### Flyway
```groovy
// BEFORE (Boot 3.x — direct dependency):
implementation 'org.flywaydb:flyway-core'
implementation 'org.flywaydb:flyway-mysql'

// AFTER (Boot 4.x — use starter):
implementation 'org.springframework.boot:spring-boot-starter-flyway'
```

#### Security Test
```groovy
// BEFORE (Boot 3.x — spring-security-test was enough for @WithMockUser):
testImplementation 'org.springframework.security:spring-security-test'

// AFTER (Boot 4.x — need explicit starter):
testImplementation 'org.springframework.boot:spring-boot-starter-security-test'
```
Note: If the project already uses `spring-security-test` and it works, keep both initially and
test. The Boot 4 starter may include the dependency transitively.

#### General pattern for Boot 4 starters
```
Module pattern:     spring-boot-<technology>
Package pattern:    org.springframework.boot.<technology>
Starter:            spring-boot-starter-<technology>
Test starter:       spring-boot-starter-<technology>-test
```

### Step 4: Migrate TestRestTemplate (IMPORTANT)

In Spring Boot 4, `TestRestTemplate` has been moved to a separate module with a new package:

#### Update imports
```java
// BEFORE (Boot 3.x):
import org.springframework.boot.test.web.client.TestRestTemplate;

// AFTER (Boot 4.x):
import org.springframework.boot.resttestclient.TestRestTemplate;
```

#### Add required dependencies
```groovy
// ADD to integration-test/build.gradle (or wherever TestRestTemplate is used):
testImplementation 'org.springframework.boot:spring-boot-resttestclient'
testImplementation 'org.springframework.boot:spring-boot-starter-restclient'
```

#### Add @AutoConfigureTestRestTemplate annotation
In Boot 4, `TestRestTemplate` is no longer auto-configured by `@SpringBootTest`. You must
explicitly add the annotation:
```java
// BEFORE (Boot 3.x — auto-configured):
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MyIntegrationTest {
    @Autowired private TestRestTemplate restTemplate;
}

// AFTER (Boot 4.x — explicit annotation):
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class MyIntegrationTest {
    @Autowired private TestRestTemplate restTemplate;
}
```

### Step 5: Fix JMS configuration changes

The `DefaultJmsListenerContainerFactoryConfigurer` has been removed in Spring Boot 4.
Replace with direct configuration:

```java
// BEFORE (Boot 3.x):
@Bean
public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
    ConnectionFactory connectionFactory,
    DefaultJmsListenerContainerFactoryConfigurer configurer) {
  final var factory = new DefaultJmsListenerContainerFactory();
  configurer.configure(factory, connectionFactory);
  factory.setSessionTransacted(true);
  return factory;
}

// AFTER (Boot 4.x):
@Bean
public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
    ConnectionFactory connectionFactory) {
  final var factory = new DefaultJmsListenerContainerFactory();
  factory.setConnectionFactory(connectionFactory);
  factory.setSessionTransacted(true);
  return factory;
}
```

Remove the import for `DefaultJmsListenerContainerFactoryConfigurer`.

### Step 6: Fix Spring Security changes

#### Remove throws Exception from SecurityFilterChain

Spring Security 7 (bundled with Spring Boot 4) no longer throws checked exceptions from
`HttpSecurity` methods:
```java
// BEFORE (Boot 3.x):
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

// AFTER (Boot 4.x):
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) {
```

#### Verify security configuration
```java
// BROKEN (old style — removed in Spring Security 6+):
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) {
        http.csrf().disable();
    }
}

// CORRECT (modern — should already be this way):
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) {
    http.csrf(csrf -> csrf.disable());
    return http.build();
}
```

Most intyg services already use the modern lambda-based DSL. Verify and fix if not.

### Step 7: Check for removed features

Scan the codebase for usage of features removed in Boot 4:

#### Undertow — REMOVED
```groovy
// If found in dependencies:
implementation 'org.springframework.boot:spring-boot-starter-undertow'
// REMOVE and use default Tomcat (spring-boot-starter-web includes Tomcat)
// Or switch to: spring-boot-starter-jetty
```

#### Spring Session Hazelcast — REMOVED
```groovy
// If found: REMOVE
implementation 'org.springframework.session:spring-session-hazelcast'
```

#### Spring Session MongoDB — REMOVED
```groovy
// If found: REMOVE
implementation 'org.springframework.session:spring-session-data-mongodb'
```

#### Embedded launch scripts — REMOVED
If `build.gradle` uses `bootJar { launchScript() }`, remove it.

#### Spock test framework — REMOVED
If any tests use Spock framework, they must be migrated to JUnit 5.

### Step 8: Fix Jakarta EE 11 / Servlet 6.1 changes

Spring Boot 4 requires Jakarta EE 11 with Servlet 6.1.

1. **Verify all imports use `jakarta.*`** (not `javax.*` for EE APIs):
   ```java
   // CORRECT:
   import jakarta.persistence.*;
   import jakarta.validation.*;
   import jakarta.jms.*;
   import jakarta.servlet.*;

   // WRONG (must be migrated):
   import javax.persistence.*;  // → jakarta.persistence.*
   import javax.validation.*;   // → jakarta.validation.*
   ```

2. **Safe javax.* packages** (do NOT change):
   - `javax.crypto.*` — java.base module
   - `javax.net.ssl.*` — java.base module
   - `javax.security.auth.*` — java.base module

3. **Servlet container compatibility**:
   - Tomcat 10.1+ required (embedded in Boot default — usually fine)
   - If deploying to external container, verify it supports Servlet 6.1

### Step 9: Check configuration properties

Some Spring Boot properties may be renamed or removed in Boot 4. Check `application.yml` and
profile-specific YAML files:

#### Known property changes
- Review `spring.activemq.*` properties — verify still valid in Boot 4
- Review `spring.jpa.*` properties — verify Hibernate compatibility
- Review `spring.datasource.*` properties — verify driver/URL compatibility
- Review `management.*` properties — actuator changes

Run the application with `--debug` flag to identify unrecognized properties:
```bash
./gradlew bootRun --args='--debug' 2>&1 | grep -i "unrecognized\|unknown\|deprecated"
```

### Step 10: Validate

Run the full build:
```bash
./gradlew clean build
```

If the build fails:
1. Check if it's a Jackson 3 import issue → update imports
2. Check if it's a starter dependency issue → update dependencies
3. Check if it's a Spring Framework 7 API change → fix code
4. Check if it's a property change → update configuration
5. Check if it's a Jakarta EE version issue → update imports

Run all tests:
```bash
./gradlew test
```

If integration tests exist in a separate module, also run them:
```bash
./gradlew integrationTest
```

### Step 11: Report results

Report:
- All dependency changes made (before → after)
- All Jackson import changes (summary of files affected)
- All code changes made and why
- All configuration changes made
- Build and test results
- Any remaining warnings or issues needing manual attention

## Real-world migration result: certificate-analytics-service

When migrating `certificate-analytics-service` from Spring Boot 3.5.10 to Spring Boot 4.0:

### Dependencies changed (app/build.gradle):
- Removed: `jackson-annotations`, `jackson-datatype-jsr310`, `liquibase-core`
- Added: `spring-boot-starter-json`, `spring-boot-starter-liquibase`
- Test fixtures: `jackson-databind` + `jackson-datatype-jsr310` → `spring-boot-starter-json`

### Dependencies changed (integration-test/build.gradle):
- Added: `spring-boot-resttestclient`, `spring-boot-starter-restclient`

### Jackson 3 migration (7 model files + parser + test data):
- All `com.fasterxml.jackson.*` imports → `tools.jackson.*`
- `new ObjectMapper()` + `JavaTimeModule` + manual config → `JsonMapper.builder().build()`
- `JsonProcessingException` → `JacksonException` (unchecked)
- Removed all `try/catch` + `UncheckedIOException` wrappers
- Added explicit `@JsonDeserialize` + `@JsonPOJOBuilder` + builder inner class to
  `PseudonymizedAnalyticsMessage` (Lombok `@Value @Builder` class)

### TestRestTemplate migration (2 files):
- Import: `o.s.b.test.web.client.TestRestTemplate` → `o.s.b.resttestclient.TestRestTemplate`
- Added `@AutoConfigureTestRestTemplate` to integration test class

### JMS configuration (1 file):
- Removed `DefaultJmsListenerContainerFactoryConfigurer` parameter and usage
- Replaced with direct `factory.setConnectionFactory(connectionFactory)`

### Security (1 file):
- Removed `throws Exception` from `securityFilterChain` method

### BOM version:
- `intygBomVersion`: updated to Spring Boot 4 BOM version

## Common patterns in intyg services

### ActiveMQ Classic configuration
Most intyg services use ActiveMQ Classic with `spring-boot-starter-activemq`. In Spring Boot 4:
- **Keep ActiveMQ Classic** — do NOT migrate to Artemis
- Verify `spring-boot-starter-activemq` is still available in Boot 4
- The JMS configuration pattern (`@JmsListener`, `JmsTemplate`) is unchanged
- **But remove `DefaultJmsListenerContainerFactoryConfigurer`** — it's been dropped

### Liquibase database migrations
Most services use `org.liquibase:liquibase-core` directly. This MUST be changed to
`spring-boot-starter-liquibase` in Boot 4.

### Security configuration
Most services have a security configuration with `SecurityFilterChain` bean using lambda DSL.
This pattern is Boot 4 compatible. Verify:
- `@EnableWebSecurity` still works (may be optional in Boot 4)
- `csrf()`, `authorizeHttpRequests()` lambda style works
- Profile-based security (e.g., testability profile) works
- **Remove `throws Exception`** from SecurityFilterChain methods

### Configuration properties
Most services use `@ConfigurationProperties` with records and `@Validated`. This is the
preferred pattern in Boot 4 and should work without changes.

## Important notes

- **Jackson 3 is the biggest change** — plan for namespace migration across all files
- **Do NOT migrate ActiveMQ Classic to Artemis** — keep Classic
- **Do NOT change dependency versions manually** — versions come from BOM
- **Do NOT refactor working code** — focus on Boot 4 compatibility only
- **Do NOT change Docker configuration** — handled externally
- If a starter is not available in Boot 4 (e.g., it was removed), report this clearly
  and let the developer decide the approach
- When in doubt about a dependency change, keep the old one AND add the new starter,
  then test. Remove the old one only after confirming the new one works.
- **This is the LAST step** in the migration sequence (after Analysis → Java 25 → Gradle 9)
