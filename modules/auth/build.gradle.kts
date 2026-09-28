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
    api(project(":modules:auth:presentation"))
    api(project(":modules:auth:domain"))
    api(project(":modules:auth:infrastructure"))
    api(project(":modules:auth:application"))
}