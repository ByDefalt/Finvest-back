plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    `java-test-fixtures`
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Exposed by the account domain types (OwnerRef uses UserId and ExternalUserId)
    api(project(":core:domain"))
    api(project(":modules:auth:api"))

    testFixturesApi(project(":core:domain"))
    testFixturesApi(libs.kotlin.test.junit5)

    testImplementation(libs.kotlin.test.junit5)
    testRuntimeOnly(libs.junit.platform.launcher)
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}
