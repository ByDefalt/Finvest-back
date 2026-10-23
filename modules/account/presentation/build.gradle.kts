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
    implementation(project(":core:presentation"))
    implementation(project(":core:utils"))
    implementation(project(":modules:account:domain"))
    implementation(project(":modules:account:application"))

    testImplementation(testFixtures(project(":modules:account:domain")))
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.jackson.module.kotlin)
    testRuntimeOnly(libs.junit.platform.launcher)
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}
