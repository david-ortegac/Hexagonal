# Project Setup

This is a Spring Boot project using Gradle Wrapper and Java 21.

## Build & Run Commands

Always use the Gradle wrapper from the project root — never a globally installed Gradle:

```bash
./gradlew build          # compile and package
./gradlew clean build    # clean then build
./gradlew bootRun        # run the application
./gradlew test           # run tests
```

## Java Version

The project requires **Java 21**. The toolchain is declared in `build.gradle`:

```groovy
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
```

Gradle will automatically use the JDK 21 toolchain. Do not override the Java version.

## Gradle Wrapper

The project uses the Gradle wrapper (`gradlew`). All commands must be run from the project root directory where `gradlew` is located:

- Project root: the directory containing `build.gradle` and `gradlew`
- Never use `cd` into subdirectories before running Gradle commands
- Use `cwd` parameter pointing to the project root when executing commands

## Project Structure

- Source: `src/main/java/co/edu/cun/tienda/tienda/`
- Tests: `src/test/java/co/edu/cun/tienda/tienda/`
- Resources: `src/main/resources/`

## Key Dependencies

- Spring Boot 4.0.5
- Spring Data JPA
- Spring Web
- Lombok (annotation processor)
- MapStruct 1.6.3 (annotation processor)
- MySQL connector
