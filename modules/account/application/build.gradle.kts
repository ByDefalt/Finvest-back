plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core:application"))
    implementation(project(":core:utils"))
    implementation(project(":modules:account:domain"))
    implementation(project(":modules:auth:api"))

    testImplementation(testFixtures(project(":modules:account:domain")))
    testImplementation(testFixtures(project(":core:application")))
    testImplementation(libs.kotlin.test.junit5)
    testRuntimeOnly(libs.junit.platform.launcher)
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}
