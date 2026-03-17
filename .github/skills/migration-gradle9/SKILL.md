---
name: migration-gradle9
description: Upgrade a Spring Boot application from Gradle 8.x to Gradle 9.4.0. Handles wrapper update, Groovy 4 compatibility fixes, plugin updates, and build script modernization. Validates with build and tests after changes.
license: MIT
---

# Gradle 9 Migration Skill

Use this skill to **upgrade a Spring Boot application from Gradle 8.x to Gradle 9.4.0**.

## Prerequisites

Before running this skill, ensure:
1. The **intyg BOM** has been updated with Gradle 9-compatible plugin versions in the version catalog
2. The `intygBomVersion` in `gradle.properties` points to the updated BOM
3. Java 17+ is available (Gradle 9 requires JVM 17+ for the daemon)

If prerequisites are not met, report what's missing and stop.

## Migration procedure

### Step 1: Capture deprecation baseline

Run the build with deprecation warnings enabled:
```bash
./gradlew help --warning-mode=all 2>&1
```

Save the output and identify all deprecation warnings. These are issues that **must** be fixed
before upgrading to Gradle 9, as deprecated APIs from Gradle 8.x are removed in 9.x.

### Step 2: Fix build script issues BEFORE upgrading wrapper

Analyze and fix all `*.gradle` files in the project. Common issues:

#### Groovy 4 compatibility (Gradle 9 ships Groovy 4.0.27)

**is-prefix boolean getters no longer work as properties:**
```groovy
// BROKEN in Groovy 4:
if (task.enabled) { }  // Only if 'enabled' is accessed via isEnabled() getter

// FIX: Usually fine — Gradle's own API handles this.
// Only a problem in custom classes with is-prefix boolean getters.
```

**DELEGATE_FIRST closure behavior changed:**
- Delegate is now preferred over owner in dynamic lookups
- Review closures that rely on owner properties being preferred
- Fix: Use explicit qualification (`project.propertyName`) or `@CompileStatic`

**Private members inaccessible in closures:**
- Subclass closures can't access parent's private members in Groovy 4
- Fix: Change visibility to `protected` or use explicit accessor methods

#### Removed APIs

**jcenter() repository — REMOVED:**
```groovy
// REMOVE:
repositories { jcenter() }
// REPLACE with:
repositories { mavenCentral() }
```

**Convention API — REMOVED:**
```groovy
// BROKEN: project.convention.getPlugin(...)
// FIX: Use project.extensions.getByType(...) instead
```

**Custom Test tasks — conventions removed:**
```groovy
// BROKEN in Gradle 9 (test convention no longer auto-configures):
tasks.register('customTest', Test) {
    // Missing testClassesDirs and classpath
}

// FIX: Explicitly configure or use test suites:
tasks.register('customTest', Test) {
    testClassesDirs = testing.suites.test.sources.output.classesDirs
    classpath = testing.suites.test.sources.runtimeClasspath
}
```

**tasks.findAll with Closure — deprecated (removal in Gradle 10):**
```groovy
// DEPRECATED:
tasks.findAll { it.name.startsWith("check") }
// REPLACE:
tasks.matching { it.name.startsWith("check") }
```

**Kotlin DSL script references — REMOVED:**
```kotlin
// BROKEN: this@Build_gradle, this@Settings_gradle
// FIX: Use project, settings, or gradle instead
```

#### Archive task changes

Gradle 9 makes archives reproducible by default (fixed file order, timestamps, permissions).
Usually this is fine, but if tests depend on specific archive behavior, they may need adjustment.

#### Settings file changes

`includeBuild()` now requires the directory to exist. If the project uses composite builds,
verify all included build directories exist.

### Step 3: Update the Gradle wrapper

After fixing build scripts, update the wrapper:

```bash
./gradlew wrapper --gradle-version 9.4.0
```

This updates both `gradle-wrapper.properties` and the wrapper JAR.

If the wrapper command fails (because current Gradle can't download 9.4.0), manually update
`gradle/wrapper/gradle-wrapper.properties`:
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-9.4.0-bin.zip
```

Then run:
```bash
./gradlew wrapper
```

### Step 4: Verify BOM version provides compatible plugins

After updating the wrapper, verify the build resolves all plugins from the version catalog.
The BOM (`se.inera.intyg.bom:catalog`) should provide Gradle 9-compatible versions for all
plugins used in the project:
- `com.diffplug.spotless`
- `com.github.ben-manes.versions`
- `org.cyclonedx.bom`
- `org.sonarqube`
- `org.springframework.boot`

If any plugin fails to resolve or is incompatible, report it.

### Step 5: Validate

Run the full build:
```bash
./gradlew clean build
```

If the build fails:
1. Read the error carefully
2. Identify if it's a Groovy 4 issue, plugin issue, or API removal
3. Fix the issue
4. Re-run the build

Once the build passes, run tests:
```bash
./gradlew test
```

### Step 6: Report results

Report:
- What files were changed and why
- Any warnings remaining (deprecated APIs scheduled for Gradle 10)
- Build and test results
- Any issues that need manual attention

## Common patterns in intyg services

These patterns are common across intyg services and have known Gradle 9 considerations:

### configureIntygBom.gradle
```groovy
configurations {
    bomProperties {
        dependencies {
            bomProperties "se.inera.intyg.bom:bom:${intygBomVersion}"
        }
        def props = new Properties()
        incoming.files.find { it.name == "bom-${intygBomVersion}.properties" }
            .withInputStream { props.load(it) }
        props.each { key, value -> project.ext."${key}" = value }
    }
}
```
This pattern uses closures and dynamic properties. Verify it works with Groovy 4.
The `props.each` closure and `incoming.files.find` should be compatible, but test carefully.

### JvmTestSuite in integration-test modules
```groovy
testing {
    suites {
        integrationTest(JvmTestSuite) {
            useJUnitJupiter()
            // ...
        }
    }
}
```
JvmTestSuite is the modern Gradle way and should work fine in Gradle 9.

### allprojects/subprojects blocks
These are still supported in Gradle 9 but considered legacy. They work fine for this migration
— converting to convention plugins is a separate effort and NOT in scope.

### Mockito agent configuration
```groovy
configurations {
    mockitoAgent
}
dependencies {
    mockitoAgent("org.mockito:mockito-core") { transitive = false }
}
test {
    jvmArgs += ["-javaagent:${configurations.mockitoAgent.asPath}"]
}
```
This pattern should work in Gradle 9. Verify `configurations.mockitoAgent.asPath` resolves
correctly.

## Important notes

- **Do NOT convert from Groovy DSL to Kotlin DSL** — that's a separate effort
- **Do NOT convert allprojects/subprojects to convention plugins** — out of scope
- **Do NOT change dependency versions** — versions come from the BOM
- **Do NOT change the ActiveMQ setup** — keep ActiveMQ Classic as-is
- Focus on making the existing build scripts work with Gradle 9.4.0
- If the BOM version needs updating, tell the developer but do not change it yourself
  unless they confirm the correct version
