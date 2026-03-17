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

### Step 2: Scan Gradle build scripts for Gradle 9 issues

Scan all `*.gradle` files in the project for:

| Issue | Pattern to detect | Severity |
|-------|------------------|----------|
| jcenter() usage | `jcenter()` in repositories blocks | HIGH — removed in Gradle 9 |
| Convention API | `convention.getPlugin`, `convention.plugins` | HIGH — removed in Gradle 9 |
| is-prefix boolean getters | Properties accessed via `is*` prefix in Groovy closures | MEDIUM — Groovy 4 change |
| buildSrc with old Kotlin | `buildSrc` using Kotlin < 2.0 | MEDIUM — needs KGP 2.0+ |
| Custom Test tasks without explicit config | `tasks.register('*', Test)` without `testClassesDirs` | MEDIUM — conventions removed |
| tasks.findAll with Closure | `tasks.findAll { }` | LOW — deprecated, use `matching` |
| Deprecated task references | `"name"()` Kotlin DSL syntax | LOW — removed |

Also check:
- `configureIntygBom.gradle` or similar custom Gradle scripts for closure compatibility
- `settings.gradle` for `includeBuild` pointing to non-existent directories

### Step 3: Scan Java source for Spring Boot 4 issues

Search all `*.java` files under `src/` for:

| Issue | Pattern to detect | Severity |
|-------|------------------|----------|
| javax.* imports (non-crypto) | `import javax.` excluding `javax.crypto` | HIGH — must be jakarta.* |
| Undertow usage | `undertow` in dependencies or imports | HIGH — removed in Boot 4 |
| Spring Session Hazelcast | `spring-session-hazelcast` | HIGH — removed in Boot 4 |
| Spring Session MongoDB | `spring-session-data-mongodb` | HIGH — removed in Boot 4 |
| Liquibase as direct dep | `org.liquibase:liquibase-core` (not via starter) | MEDIUM — needs starter in Boot 4 |
| Flyway as direct dep | `org.flywaydb:flyway-core` (not via starter) | MEDIUM — needs starter in Boot 4 |
| @WithMockUser without starter | `@WithMockUser` usage + no `spring-boot-starter-security-test` | MEDIUM — needs starter in Boot 4 |
| WebSecurityConfigurerAdapter | `WebSecurityConfigurerAdapter` | HIGH — removed (should use SecurityFilterChain) |
| Non-lambda security DSL | `http.csrf().disable()` (non-lambda style) | MEDIUM — should use lambda DSL |
| @Value for config | `@Value("${` annotations | LOW — prefer @ConfigurationProperties |
| Deprecated Spring APIs | Check for known deprecated method calls | MEDIUM |

### Step 4: Scan Java source for Java 25 issues

Search all `*.java` files under `src/` for:

| Issue | Pattern to detect | Severity |
|-------|------------------|----------|
| sun.* internal APIs | `import sun.` | HIGH — may be encapsulated |
| com.sun.* internal APIs | `import com.sun.` (not com.sun.jna etc.) | HIGH — may be encapsulated |
| Reflection on JDK internals | `setAccessible(true)` on JDK classes | MEDIUM — stronger encapsulation |
| Deprecated Date/Calendar | `java.util.Date`, `java.util.Calendar` | LOW — still works but modernize |
| Finalize methods | `protected void finalize()` | MEDIUM — deprecated for removal |
| Security Manager | `System.setSecurityManager`, `SecurityManager` | HIGH — removed |

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

## Phase 1: Gradle 9 Upgrade
### Issues Found
- [list each issue with file, line, severity, and fix description]
### Estimated Effort: [LOW/MEDIUM/HIGH]

## Phase 2: Java 25 Upgrade
### Issues Found
- [list each issue]
### Estimated Effort: [LOW/MEDIUM/HIGH]

## Phase 3: Spring Boot 4 Upgrade
### Issues Found
- [list each issue]
### Estimated Effort: [LOW/MEDIUM/HIGH]

## Summary
| Phase | Issues | Effort |
|-------|--------|--------|
| Gradle 9 | N | LOW/MEDIUM/HIGH |
| Java 25 | N | LOW/MEDIUM/HIGH |
| Spring Boot 4 | N | LOW/MEDIUM/HIGH |

## Recommended Migration Order
[Based on dependencies and risk, recommend which phase to do first]
```

## Important notes

- **Do NOT modify any files.** This skill is analysis-only.
- **Do NOT create files** in the project directory. Output the report to the console only.
- If you cannot determine a version from the BOM, note it as "Unknown (BOM-managed)" and suggest
  the developer check the BOM.
- `javax.crypto.*` and `javax.net.ssl.*` are part of `java.base` module — these are NOT Jakarta EE
  and do NOT need migration. Do not flag these as issues.
- Focus on actionable findings. Don't report things that are already correct as issues.
