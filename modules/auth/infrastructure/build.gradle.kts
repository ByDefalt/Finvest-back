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
    implementation(project(":core:infrastructure"))
    implementation(project(":core:utils"))
    implementation(project(":modules:auth:domain"))
    implementation(project(":modules:auth:application"))
    implementation(project(":modules:auth:api"))

    testImplementation(testFixtures(project(":core:infrastructure")))
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
