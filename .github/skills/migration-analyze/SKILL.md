---
name: migration-analyze
description: Analyze a Spring Boot application for migration readiness. Scans build files, source code, and configuration to produce a detailed migration report covering Gradle 9, Java 25, and Spring Boot 4 upgrade paths. Read-only — makes no changes to the codebase.
license: MIT
---

# Migration Analysis Skill

Use this skill to **analyze a Spring Boot application's migration readiness** before upgrading to Gradle 9, Java 25, and Spring Boot 4.

This skill is **read-only** — it produces a report but does not modify any files.

## When to use

- Before starting any migration work on a service
- To get an overview of what needs to change and the estimated effort
- To verify prerequisites (BOM version, Docker images) are in place

## Recommended migration order

Based on real-world experience migrating `certificate-analytics-service`, the recommended order is:

1. **Analysis** (this skill) — read-only report of what needs to change
2. **Java 25** (`migration-java25`) — typically zero code changes for BOM-managed projects
3. **Gradle 9** (`migration-gradle9`) — build script fixes, wrapper update
4. **Spring Boot 4** (`migration-spring-boot4`) — most work: Jackson 3, starters, test utilities

This order minimizes risk: Java 25 is usually a no-op, Gradle 9 is build-only changes, and
Spring Boot 4 (with Jackson 3) is the most involved step that benefits from a stable foundation.

## Analysis procedure

Follow these steps in order. For each step, collect findings into a structured report.

### Step 1: Detect current versions

Read these files and extract version information:

1. **`gradle/wrapper/gradle-wrapper.properties`** → Extract Gradle version from `distributionUrl`
2. **`gradle.properties`** → Extract `intygBomVersion` (or equivalent BOM version property)
3. **`build.gradle`** (root) → Identify plugins, dependency management approach
4. **BOM properties** → The BOM (`se.inera.intyg.bom:platform:${intygBomVersion}`) provides:
   - `javaVersion` — current Java toolchain version
   - `springBootVersion` — current Spring Boot version
   - `googleJavaFormatVersion` — formatter version
   - Plugin versions via version catalog (`se.inera.intyg.bom:catalog`)

If BOM properties cannot be resolved directly, note this and scan `build.gradle` for explicit
version declarations.

### Step 2: Scan Java source for Java 25 issues

Search all `*.java` files under `src/` for:

| Issue | Pattern to detect | Severity |
|-------|------------------|----------|
| sun.* internal APIs | `import sun.` | HIGH — may be encapsulated |
| com.sun.* internal APIs | `import com.sun.` (not com.sun.jna etc.) | HIGH — may be encapsulated |
| Reflection on JDK internals | `setAccessible(true)` on JDK classes | MEDIUM — stronger encapsulation |
| Deprecated Date/Calendar | `java.util.Date`, `java.util.Calendar` | LOW — still works but modernize |
| Finalize methods | `protected void finalize()` | MEDIUM — deprecated for removal |
| Security Manager | `System.setSecurityManager`, `SecurityManager` | HIGH — removed |

**Note:** In practice, BOM-managed projects using standard libraries (Lombok, Mockito, Spring)
typically require **zero code changes** for Java 25. This step is verification, not expectation.

### Step 3: Scan Gradle build scripts for Gradle 9 issues

Scan all `*.gradle` files in the project for:

| Issue | Pattern to detect | Severity |
|-------|------------------|----------|
| jcenter() usage | `jcenter()` in repositories blocks | HIGH — removed in Gradle 9 |
| Convention API | `convention.getPlugin`, `convention.plugins` | HIGH — removed in Gradle 9 |
| Maven URL without equals | `maven { url "..." }` (missing `=` sign) | HIGH — must be `url = "..."` in Gradle 9 |
| Spotless googleJavaFormat ordering | `googleJavaFormat()` after `removeUnusedImports()` | HIGH — must come before, otherwise uses bundled version |
| is-prefix boolean getters | Properties accessed via `is*` prefix in Groovy closures | MEDIUM — Groovy 4 change |
| buildSrc with old Kotlin | `buildSrc` using Kotlin < 2.0 | MEDIUM — needs KGP 2.0+ |
| Custom Test tasks without explicit config | `tasks.register('*', Test)` without `testClassesDirs` | MEDIUM — conventions removed |
| tasks.findAll with Closure | `tasks.findAll { }` | LOW — deprecated, use `matching` |
| Deprecated task references | `"name"()` Kotlin DSL syntax | LOW — removed |
| Custom repo without content filter | `maven { url = "..." }` without `content {}` block | LOW — recommended to add content filtering |

Also check:
- `configureIntygBom.gradle` or similar custom Gradle scripts for closure compatibility
- `settings.gradle` for `includeBuild` pointing to non-existent directories
- `googleJavaFormatVersion` — needs to be at least `1.35.0` for Gradle 9 compatibility

### Step 4: Scan Java source for Spring Boot 4 issues

Search all `*.java` files under `src/` for:

| Issue | Pattern to detect | Severity |
|-------|------------------|----------|
| javax.* imports (non-crypto) | `import javax.` excluding `javax.crypto` | HIGH — must be jakarta.* |
| Jackson 2 imports | `import com.fasterxml.jackson.*` | HIGH — must migrate to `tools.jackson.*` (Jackson 3) |
| Jackson ObjectMapper construction | `new ObjectMapper()` with manual module registration | HIGH — replace with `JsonMapper.builder().build()` |
| Jackson JavaTimeModule | `JavaTimeModule` registration | MEDIUM — no longer needed in Jackson 3 |
| Jackson JsonProcessingException | `catch (JsonProcessingException` or `throws JsonProcessingException` | MEDIUM — now `JacksonException` (unchecked in Jackson 3) |
| Lombok @Value/@Builder with Jackson | `@Value @Builder` classes using `@JsonDeserialize` | HIGH — need Jackson 3 annotations + explicit `@JsonPOJOBuilder` inner class |
| Undertow usage | `undertow` in dependencies or imports | HIGH — removed in Boot 4 |
| Spring Session Hazelcast | `spring-session-hazelcast` | HIGH — removed in Boot 4 |
| Spring Session MongoDB | `spring-session-data-mongodb` | HIGH — removed in Boot 4 |
| Liquibase as direct dep | `org.liquibase:liquibase-core` (not via starter) | HIGH — needs `spring-boot-starter-liquibase` in Boot 4 |
| Flyway as direct dep | `org.flywaydb:flyway-core` (not via starter) | HIGH — needs `spring-boot-starter-flyway` in Boot 4 |
| Explicit Jackson deps | `jackson-annotations`, `jackson-databind`, `jackson-datatype-jsr310` | MEDIUM — replace with `spring-boot-starter-json` |
| TestRestTemplate (old package) | `import org.springframework.boot.test.web.client.TestRestTemplate` | HIGH — moved to `org.springframework.boot.resttestclient` |
| DefaultJmsListenerContainerFactoryConfigurer | `DefaultJmsListenerContainerFactoryConfigurer` | HIGH — removed in Boot 4, use `setConnectionFactory()` directly |
| @WithMockUser without starter | `@WithMockUser` usage + no `spring-boot-starter-security-test` | MEDIUM — needs starter in Boot 4 |
| WebSecurityConfigurerAdapter | `WebSecurityConfigurerAdapter` | HIGH — removed (should use SecurityFilterChain) |
| Non-lambda security DSL | `http.csrf().disable()` (non-lambda style) | MEDIUM — should use lambda DSL |
| SecurityFilterChain throws Exception | `SecurityFilterChain ... throws Exception` | LOW — no longer throws checked exception |
| @Value for config | `@Value("${` annotations | LOW — prefer @ConfigurationProperties |
| Deprecated Spring APIs | Check for known deprecated method calls | MEDIUM |

### Step 5: Check Docker and DevOps configuration

Scan for:
- `Dockerfile` — check `FROM` image Java version
- `devops/` directory — check for base image references
- `Jenkins.properties` or CI config — check Java/Gradle version references
- Verify Dockerfile uses `ARG from_image` pattern (version controlled externally) vs hardcoded

### Step 6: Check dependencies in build.gradle files

For each `build.gradle`, list all dependencies and flag:
- Dependencies that need starter migration in Boot 4
- Dependencies that may be incompatible with Java 25
- Test dependencies that need updating
- Testcontainers version (should be recent for Java 25 support)
- Jackson dependencies that need namespace migration
- TestRestTemplate dependencies that need resttestclient module

### Step 7: Produce the migration report

Output a structured report in this format:

```markdown
# Migration Analysis Report: [project-name]

## Current Versions
| Component | Version | Source |
|-----------|---------|--------|
| Gradle | X.Y.Z | gradle-wrapper.properties |
| Java | XX | BOM (intygBomVersion=X.Y.Z) |
| Spring Boot | X.Y.Z | BOM |
| BOM | X.Y.Z | gradle.properties |

## Prerequisites Status
| Prerequisite | Status | Details |
|-------------|--------|---------|
| Updated BOM available | ✅/❌ | intygBomVersion=X.Y.Z provides SB4/Java25 |
| Docker images updated | ✅/❌ | Base image supports Java 25 |

## Phase 1: Java 25 Upgrade
### Issues Found
- [list each issue with file, line, severity, and fix description]
### Estimated Effort: [LOW/MEDIUM/HIGH]
### Note: BOM-managed projects typically require zero code changes for Java 25.

## Phase 2: Gradle 9 Upgrade
### Issues Found
- [list each issue with file, line, severity, and fix description]
### Estimated Effort: [LOW/MEDIUM/HIGH]

## Phase 3: Spring Boot 4 Upgrade
### Issues Found
- [list each issue with file, line, severity, and fix description]
### Estimated Effort: [LOW/MEDIUM/HIGH]
### Key areas: Jackson 3 migration, starter restructuring, TestRestTemplate relocation

## Summary
| Phase | Issues | Effort |
|-------|--------|--------|
| Java 25 | N | LOW/MEDIUM/HIGH |
| Gradle 9 | N | LOW/MEDIUM/HIGH |
| Spring Boot 4 | N | LOW/MEDIUM/HIGH |

## Recommended Migration Order
1. **Java 25** — typically zero code changes for BOM-managed projects
2. **Gradle 9** — build script fixes only, no source code changes
3. **Spring Boot 4** — most involved: Jackson 3 namespace migration, starter restructuring, test utility relocation
```

## Important notes

- **Do NOT modify any files.** This skill is analysis-only.
- **Do NOT create files** in the project directory. Output the report to the console only.
- If you cannot determine a version from the BOM, note it as "Unknown (BOM-managed)" and suggest
  the developer check the BOM.
- `javax.crypto.*` and `javax.net.ssl.*` are part of `java.base` module — these are NOT Jakarta EE
  and do NOT need migration. Do not flag these as issues.
- Focus on actionable findings. Don't report things that are already correct as issues.
