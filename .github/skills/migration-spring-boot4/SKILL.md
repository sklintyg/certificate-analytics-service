---
name: migration-spring-boot4
description: Upgrade a Spring Boot application from Spring Boot 3.5.x to Spring Boot 4.x. Handles starter dependency restructuring, Spring Framework 7.x changes, Jakarta EE 11 updates, and configuration property migrations. Validates with build and tests after changes.
license: MIT
---

# Spring Boot 4 Migration Skill

Use this skill to **upgrade a Spring Boot application from Spring Boot 3.5.x to Spring Boot 4.x**.

## Prerequisites

Before running this skill, ensure:
1. The **intyg BOM** has been updated with Spring Boot 4.x and compatible 3pp versions
2. The `intygBomVersion` in `gradle.properties` points to the updated BOM
3. **Gradle 9** is already in place (run `migration-gradle9` skill first)
4. **Java 25** is already in place (run `migration-java25` skill first)

If prerequisites are not met, report what's missing and stop.

## Migration procedure

### Step 1: Verify BOM provides Spring Boot 4.x

1. Check `gradle.properties` → `intygBomVersion` value
2. Verify the BOM provides Spring Boot 4.x (the version catalog should include Boot 4 plugins)
3. If BOM is not updated, report and stop

### Step 2: Update dependency starters (CRITICAL)

Spring Boot 4 restructured its modules. Some dependencies that were previously optional or
pulled in transitively now require **explicit starter dependencies**.

Scan all `build.gradle` files and apply these changes:

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

#### JMS Test (if integration tests use JMS)
```groovy
// NEW in Boot 4.x — if tests need JMS support:
testImplementation 'org.springframework.boot:spring-boot-starter-jms-test'
```

#### Validation
```groovy
// Check if using validation — starter name unchanged but verify:
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

#### General pattern for Boot 4 starters
```
Module pattern:     spring-boot-<technology>
Package pattern:    org.springframework.boot.<technology>
Starter:            spring-boot-starter-<technology>
Test starter:       spring-boot-starter-<technology>-test
```

### Step 3: Check for removed features

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
// Replace with native Hazelcast session support
```

#### Spring Session MongoDB — REMOVED
```groovy
// If found: REMOVE
implementation 'org.springframework.session:spring-session-data-mongodb'
// Replace with native MongoDB session support
```

#### Embedded launch scripts — REMOVED
If `build.gradle` uses:
```groovy
bootJar {
    launchScript()
}
```
Remove the `launchScript()` call. Use Gradle application plugin or container-based deployment.

#### Spock test framework — REMOVED
If any tests use Spock framework, they must be migrated to JUnit 5.

### Step 4: Fix Spring Framework 7.x breaking changes

Spring Boot 4 ships Spring Framework 7.x. Check for:

#### Security configuration
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
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable());
    return http.build();
}
```

Most intyg services already use the modern lambda-based DSL. Verify and fix if not.

#### HttpStatusCode changes
Spring Framework 7 may have changes to `HttpStatusCode` handling. Verify:
- `ResponseStatusException` usage still compiles
- Custom error handlers work correctly

#### Property binding
Spring Framework 7 may have stricter property binding. Check:
- `@ConfigurationProperties` classes bind correctly
- No relaxed binding edge cases

### Step 5: Fix Jakarta EE 11 / Servlet 6.1 changes

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

### Step 6: Check configuration properties

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

### Step 7: Update test configurations

1. **Testcontainers** — verify version supports Spring Boot 4 test context
2. **@SpringBootTest** — verify annotation behavior unchanged
3. **TestRestTemplate** — verify still available and working
4. **MockMvc** — if used, verify with `spring-boot-starter-webmvc-test` if needed
5. **ActiveMQ test containers** — verify JMS configuration still works

### Step 8: Validate

Run the full build:
```bash
./gradlew clean build
```

If the build fails:
1. Check if it's a starter dependency issue → update dependencies
2. Check if it's a Spring Framework 7 API change → fix code
3. Check if it's a property change → update configuration
4. Check if it's a Jakarta EE version issue → update imports

Run all tests:
```bash
./gradlew test
```

If integration tests exist in a separate module, also run them:
```bash
./gradlew integrationTest
```

### Step 9: Report results

Report:
- All dependency changes made (before → after)
- All code changes made and why
- All configuration changes made
- Build and test results
- Any remaining warnings or issues needing manual attention
- Confirmation of which Boot 4 features are now available

## Common patterns in intyg services

### ActiveMQ Classic configuration
Most intyg services use ActiveMQ Classic with `spring-boot-starter-activemq`. In Spring Boot 4:
- **Keep ActiveMQ Classic** — do NOT migrate to Artemis
- Verify `spring-boot-starter-activemq` is still available in Boot 4
- If the starter was renamed or reorganized, update accordingly
- The JMS configuration pattern (`@JmsListener`, `JmsTemplate`) should be unchanged

### Liquibase database migrations
Most services use `org.liquibase:liquibase-core` directly. This MUST be changed to
`spring-boot-starter-liquibase` in Boot 4.

### Security configuration
Most services have a security configuration with `SecurityFilterChain` bean using lambda DSL.
This pattern is Boot 4 compatible. Verify:
- `@EnableWebSecurity` still works (may be optional in Boot 4)
- `csrf()`, `authorizeHttpRequests()` lambda style works
- Profile-based security (e.g., testability profile) works

### Configuration properties
Most services use `@ConfigurationProperties` with records and `@Validated`. This is the
preferred pattern in Boot 4 and should work without changes.

## Important notes

- **Do NOT migrate ActiveMQ Classic to Artemis** — keep Classic
- **Do NOT change dependency versions manually** — versions come from BOM
- **Do NOT refactor working code** — focus on Boot 4 compatibility only
- **Do NOT change Docker configuration** — handled externally
- If a starter is not available in Boot 4 (e.g., it was removed), report this clearly
  and let the developer decide the approach
- When in doubt about a dependency change, keep the old one AND add the new starter,
  then test. Remove the old one only after confirming the new one works.
