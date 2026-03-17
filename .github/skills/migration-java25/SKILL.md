---
name: migration-java25
description: Upgrade a Spring Boot application from Java 21 to Java 25. Scans for JDK API incompatibilities, updates toolchain configuration, and validates the build compiles and tests pass with the new Java version.
license: MIT
---

# Java 25 Migration Skill

Use this skill to **upgrade a Spring Boot application from Java 21 to Java 25**.

## Prerequisites

Before running this skill, ensure:
1. The **intyg BOM** has been updated with `javaVersion=25`
2. The `intygBomVersion` in `gradle.properties` points to the updated BOM
3. **Gradle 9** is already in place (run `migration-gradle9` skill first)
4. **Java 25 JDK** is installed and available on the system
5. **Docker builder/runtime images** have been updated to Java 25 (handled externally)

If prerequisites are not met, report what's missing and stop.

## Migration procedure

### Step 1: Verify Java toolchain configuration

Check that the Java version is configured correctly:

1. Read `gradle.properties` → verify `intygBomVersion` is the updated version
2. Check `build.gradle` → the toolchain should reference `javaVersion` from BOM properties:
   ```groovy
   java {
       toolchain {
           languageVersion = JavaLanguageVersion.of(javaVersion)
       }
   }
   ```
3. If `javaVersion` is hardcoded instead of coming from BOM, update it to `25`
4. Verify `JAVA_HOME` or Gradle toolchain auto-detection can find Java 25

### Step 2: Scan for Java 25 incompatibilities

Search all `*.java` files under `src/` for potential issues:

#### HIGH severity — likely to break

| Pattern | Description | Fix |
|---------|-------------|-----|
| `import sun.*` | Internal JDK API | Replace with public API equivalent |
| `import com.sun.*` (internal) | Internal JDK API | Replace with public API. Note: `com.sun.jna` and similar 3pp are fine |
| `SecurityManager` usage | Removed for removal since Java 17, fully removed path | Remove or replace with alternative security mechanism |
| `System.setSecurityManager()` | Security Manager removed | Remove |
| `protected void finalize()` | Deprecated for removal | Use `Cleaner` or try-with-resources |
| `Runtime.runFinalizersOnExit()` | Removed | Remove |

#### MEDIUM severity — may need attention

| Pattern | Description | Fix |
|---------|-------------|-----|
| `.setAccessible(true)` on JDK classes | Stronger module encapsulation | May need `--add-opens` JVM arg or alternative approach |
| `Unsafe` usage | `sun.misc.Unsafe` access restricted | Use `VarHandle` or `MemorySegment` API |
| Thread-related deprecated methods | `Thread.stop()`, `Thread.suspend()`, `Thread.resume()` | Use modern concurrency patterns |
| Deprecated `URL` constructors | `new URL(string)` deprecated | Use `URI.create(string).toURL()` |

#### LOW severity — still works but modernize when possible

| Pattern | Description | Fix |
|---------|-------------|-----|
| `java.util.Date` / `Calendar` | Legacy date API | Use `java.time.*` (not blocking) |
| Raw types | Generic types without parameters | Add type parameters |

#### Safe patterns — do NOT flag as issues

| Pattern | Why it's safe |
|---------|--------------|
| `javax.crypto.*` | Part of `java.base` module, not Jakarta EE |
| `javax.net.ssl.*` | Part of `java.base` module, not Jakarta EE |
| `javax.security.auth.*` | Part of `java.base` module |
| `javax.xml.crypto.*` | Part of `java.xml.crypto` module |

### Step 3: Check JVM arguments

Review test and runtime JVM arguments for compatibility:

1. Check `build.gradle` `test` blocks for JVM args:
   ```groovy
   test {
       jvmArgs += [
           "-javaagent:${configurations.mockitoAgent.asPath}",
           "-Xshare:off"
       ]
   }
   ```
   - `-javaagent` for Mockito: Should work, but verify Mockito version supports Java 25
   - `-Xshare:off`: May not be needed in Java 25, but won't break

2. Check `bootRun` blocks for debug/runtime JVM args
3. Check `Dockerfile` / `launch.sh` for JVM options (`$JVM_OPTS`, `$JAVA_OPTS`)

### Step 4: Check Lombok compatibility

If the project uses Lombok:
1. Verify Lombok version supports Java 25 (check BOM provides compatible version)
2. Lombok with newer Java versions sometimes needs updates for new bytecode features
3. If Lombok version is too old, report that BOM needs a Lombok update

### Step 5: Check test framework compatibility

1. **JUnit 5** — should work with Java 25
2. **Mockito** — verify version supports Java 25 (Mockito uses bytecode generation)
   - The `-javaagent` approach for Mockito is the modern way and should be compatible
3. **Testcontainers** — verify version supports Java 25 runtime
4. **Spring Boot Test** — version compatibility handled by BOM

### Step 6: Validate

Run the full build:
```bash
./gradlew clean build
```

If compilation fails:
1. Check if it's a removed API issue → fix the code
2. Check if it's a module access issue → add `--add-opens` or refactor
3. Check if it's a Lombok issue → report BOM needs Lombok update

Run tests:
```bash
./gradlew test
```

If tests fail:
1. Check for reflection-related failures (stronger encapsulation)
2. Check for Mockito agent issues
3. Check for date/time format changes in assertions

### Step 7: Report results

Report:
- Java version before and after
- Any source code changes made and why
- Any JVM argument changes
- Build and test results
- Any issues that need manual attention (e.g., BOM Lombok version)

## Java 21 → 25 new features (informational)

These are new features available after migration. Do NOT refactor code to use them unless the
developer asks — this skill is about compatibility, not modernization.

- **Pattern matching enhancements** (records, sealed classes)
- **Virtual threads** (Project Loom — available since Java 21)
- **String templates** (preview features)
- **Structured concurrency** (preview/incubator)
- **Scoped values** (preview)
- **Foreign Function & Memory API** (finalized)

## Important notes

- **Do NOT refactor code to use new Java 25 features** — focus on compatibility only
- **Do NOT change dependency versions** — versions come from the BOM
- **Do NOT change Docker base images** — handled externally
- **`javax.crypto.*` is safe** — it's part of `java.base`, not Jakarta EE
- If the BOM doesn't provide Java 25-compatible library versions, report this but don't
  attempt to override individual library versions
