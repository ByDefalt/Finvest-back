plugins {
    `java-library`
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":core:domain"))
    api(project(":core:infrastructure"))
    api(project(":core:application"))
    api(project(":core:presentation"))
    api(project(":core:utils"))
}