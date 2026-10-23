plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.ktlint)
    `java-test-fixtures`
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

    // Shared integration test tooling (MariaDB Testcontainer, JDBC slice base class),
    // consumed by other modules with testImplementation(testFixtures(project(":core:infrastructure")))
    testFixturesApi(libs.spring.boot.starter.test)
    testFixturesApi(libs.spring.boot.starter.data.jdbc.test)
    testFixturesApi(libs.testcontainers.mariadb)
    testFixturesApi(libs.spring.boot.testcontainers)

    testImplementation(libs.spring.boot.starter.security.test)
    testImplementation(libs.kotlin.test.junit5)
    testRuntimeOnly(libs.junit.platform.launcher)
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}

// Flyway migrations live in the app module: expose them to this module's tests only
tasks.processTestResources {
    from(rootProject.file("app/src/main/resources/db/migration")) { into("db/migration") }
}
