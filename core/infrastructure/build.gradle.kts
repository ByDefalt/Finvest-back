plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.ktlint)
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":core:domain"))
    api(project(":core:application"))
    api(libs.spring.boot.starter.data.jdbc)
    api(libs.spring.boot.starter.actuator)
    api(libs.spring.boot.starter.security)
    api(libs.spring.boot.h2console)
    api(libs.flyway)
    api(libs.flyway.mysql)
    api(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)
    runtimeOnly(libs.h2)
    runtimeOnly(libs.mariadb)

    testImplementation(libs.spring.boot.starter.data.jdbc.test)
    testImplementation(libs.spring.boot.starter.security.test)
    testImplementation(libs.testcontainers.mariadb)
    testImplementation(libs.spring.boot.testcontainers)
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}