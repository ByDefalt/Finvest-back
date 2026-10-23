import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.dokka)
    jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

val springBootVersion = libs.versions.spring.boot.get()
val jacocoVersion = libs.versions.jacoco.get()

jacoco {
    toolVersion = jacocoVersion
}

subprojects {
    apply(plugin = "io.spring.dependency-management")

    extensions.configure<DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
        }
    }

    // Every Kotlin module records the coverage of its tests (build/jacoco/test.exec) and documents its API
    plugins.withId("org.jetbrains.kotlin.jvm") {
        apply(plugin = "jacoco")
        extensions.configure<JacocoPluginExtension> {
            toolVersion = jacocoVersion
        }

        apply(plugin = "org.jetbrains.dokka")
        extensions.configure<DokkaExtension> {
            // Several modules share a name (domain, application...): name them after their path (account:domain)
            moduleName.set(path.removePrefix(":").removePrefix("modules:"))
            dokkaSourceSets.configureEach {
                documentedVisibilities(VisibilityModifier.Public, VisibilityModifier.Protected, VisibilityModifier.Internal)
                reportUndocumented.set(true)
            }
        }
    }

    afterEvaluate {
        group = "com.example.finvest${path.replace(":", ".")}"
    }
}

// ------------------------------------------------------------------
// Coverage report of the whole project: ./gradlew jacocoRootReport
// ------------------------------------------------------------------

/** Kotlin modules with production code (the aggregator projects :core, :modules... have none). */
val coveredProjects = subprojects.filter { it.file("src/main/kotlin").isDirectory }

tasks.register<JacocoReport>("jacocoRootReport") {
    group = "verification"
    description = "Runs the tests of every module and generates one coverage report for the whole project."

    // The tests of a module also cover the code of the modules it depends on (e.g. app end-to-end tests)
    dependsOn(coveredProjects.map { "${it.path}:test" })

    // Modules without tests have no execution data: the missing files are ignored
    executionData.from(
        coveredProjects.map { project ->
            files(project.layout.buildDirectory.file("jacoco/test.exec")).filter { it.exists() }
        },
    )
    sourceDirectories.from(coveredProjects.map { it.file("src/main/kotlin") })
    classDirectories.from(
        coveredProjects.map { project ->
            project.layout.buildDirectory.dir("classes/kotlin/main").map { classes ->
                project.fileTree(classes) {
                    // Spring Boot entry point, nothing to test there
                    exclude("defalt/BackApplication*", "defalt/FinvestApplicationKt*")
                }
            }
        },
    )

    reports {
        html.required = true
        xml.required = true
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco/root/html")
        xml.outputLocation = layout.buildDirectory.file("reports/jacoco/root/jacocoRootReport.xml")
    }
}

// ------------------------------------------------------------------
// Documentation of the whole project: ./gradlew dokkaGenerate (build/dokka/html/index.html)
// ------------------------------------------------------------------

dokka {
    moduleName.set("Finvest")
    // Undocumented public API or unresolved [link]: the documentation fails instead of silently degrading
    dokkaPublications.html {
        failOnWarning.set(true)
    }
}

dependencies {
    coveredProjects.forEach { dokka(it) }
}
