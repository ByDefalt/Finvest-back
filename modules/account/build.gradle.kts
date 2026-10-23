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
    api(project(":modules:account:domain"))
    api(project(":modules:account:application"))
    api(project(":modules:account:infrastructure"))
    api(project(":modules:account:presentation"))
}