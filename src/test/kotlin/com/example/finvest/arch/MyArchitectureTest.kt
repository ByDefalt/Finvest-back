package com.example.finvest.arch

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.RestController

class MyArchitectureTest {
    private val importedClasses = ClassFileImporter().importPackages("com.example.finvest")

    @Test
    fun repositoriesFollowThePersistenceConvention() {
        classes().that().areAnnotatedWith(Repository::class.java)
            .should().resideInAPackage("..infrastructure.persistence.repository..")
            .andShould().haveSimpleNameEndingWith("Repository")
            .check(importedClasses)
    }

    @Test
    fun controllersFollowThePresentationConvention() {
        classes().that().areAnnotatedWith(RestController::class.java)
            .should().resideInAPackage("..presentation.controller..")
            .andShould().haveSimpleNameEndingWith("Controller")
            .check(importedClasses)
    }

    @Test
    fun configurationsAreKeptInConfigurationPackages() {
        classes().that().areAnnotatedWith(Configuration::class.java)
            .should().resideInAPackage("..config..")
            .check(importedClasses)
    }

    @Test
    fun domainDoesNotDependOnOuterLayers() {
        noClasses().that().resideInAnyPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                "..application..",
                "..infrastructure..",
                "..presentation.."
            )
            .check(importedClasses)
    }

    @Test
    fun domainDoesNotDependOnFrameworks() {
        noClasses().that().resideInAnyPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("org.springframework..", "jakarta..")
            .check(importedClasses)
    }

    @Test
    fun useCasesFollowTheApplicationConvention() {
        classes().that().resideInAnyPackage("..application.usecase..")
            .should().haveSimpleNameEndingWith("UseCase")
            .check(importedClasses)
    }

    @Test
    fun applicationDoesNotDependOnInfrastructureOrPresentation() {
        noClasses().that().resideInAnyPackage("..application..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..infrastructure..", "..presentation..")
            .check(importedClasses)
    }

    @Test
    fun presentationDoesNotDependOnInfrastructure() {
        noClasses().that().resideInAnyPackage("..presentation..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..infrastructure..")
            .check(importedClasses)
    }

    @Test
    fun infrastructureDoesNotDependOnPresentation() {
        noClasses().that().resideInAnyPackage("..infrastructure..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..presentation..")
            .check(importedClasses)
    }

    @Test
    fun domainRepositoriesFollowThePortConvention() {
        classes().that().resideInAnyPackage("..domain.repository..")
            .should().haveSimpleNameEndingWith("Repository")
            .check(importedClasses)
    }
}