plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.ktlint)
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":modules"))

    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.archunit)
    developmentOnly(libs.spring.boot.docker.compose)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.starter.webmvc.test)
    testImplementation(testFixtures(project(":core:infrastructure")))
    testRuntimeOnly(libs.junit.platform.launcher)
}

kotlin {
    jvmToolchain(25)
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-Xannotation-default-target=param-property",
        )
    }
}

tasks.test {
    useJUnitPlatform()
}

sourceSets {
    main {
        resources.srcDir(rootProject.file("src/main/resources"))
    }
}
