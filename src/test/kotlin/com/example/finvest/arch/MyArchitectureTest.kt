package com.example.finvest.arch

import com.example.finvest.modules.shared.presentation.security.AuthenticationRequired
import com.example.finvest.modules.shared.presentation.security.PublicEndpoint
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.domain.JavaField
import com.tngtech.archunit.core.domain.JavaModifier
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import com.tngtech.archunit.library.GeneralCodingRules.ACCESS_STANDARD_STREAMS
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

class MyArchitectureTest {
    private companion object {
        const val ROOT = "com.example.finvest"

        // Seuils SOLID (à ajuster si besoin)
        const val MAX_PORT_METHODS = 5
        const val MAX_CONSTRUCTOR_DEPENDENCIES = 5

        // Modules transverses que tout le monde a le droit d'utiliser
        val SHARED_MODULES = setOf("shared", "common")
        val MODULE_NAME = Regex("""^com\.example\.finvest\.modules\.([^.]+)\.""")

        // Les tests sont exclus : seul le code de production est analysé
        val importedClasses: JavaClasses =
            ClassFileImporter()
                .withImportOption(ImportOption.DoNotIncludeTests())
                .importPackages(ROOT)

        val FRAMEWORK_PACKAGES =
            arrayOf(
                "org.springframework..",
                "jakarta..",
                "java.sql..",
                "javax.sql..",
                "tools.jackson..",
                "com.fasterxml..",
            )
    }

    // ------------------------------------------------------------------
    // Conventions de placement et de nommage
    // ------------------------------------------------------------------

    @Test
    fun repositoriesFollowThePersistenceConvention() {
        classes()
            .that()
            .areAnnotatedWith(Repository::class.java)
            .should()
            .resideInAPackage("..infrastructure.persistence.repository..")
            .andShould()
            .haveSimpleNameEndingWith("Repository")
            .check(importedClasses)
    }

    @Test
    fun controllersFollowThePresentationConvention() {
        classes()
            .that()
            .areAnnotatedWith(RestController::class.java)
            .should()
            .resideInAPackage("..presentation.controller..")
            .andShould()
            .haveSimpleNameEndingWith("Controller")
            .check(importedClasses)
    }

    @Test
    fun exceptionHandlersLiveInPresentation() {
        classes()
            .that()
            .areAnnotatedWith(RestControllerAdvice::class.java)
            .should()
            .resideInAPackage("..presentation.exception..")
            .andShould()
            .haveSimpleNameEndingWith("ExceptionHandler")
            .check(importedClasses)
    }

    @Test
    fun configurationsAreCompositionRoots() {
        classes()
            .that()
            .areAnnotatedWith(Configuration::class.java)
            .should()
            .resideInAnyPackage("..infrastructure.config..", "$ROOT.config..")
            .check(importedClasses)
    }

    @Test
    fun useCasesFollowTheApplicationConvention() {
        classes()
            .that()
            .resideInAnyPackage("..application.usecase..")
            .should()
            .haveSimpleNameEndingWith("UseCase")
            .check(importedClasses)
    }

    @Test
    fun domainRepositoriesFollowThePortConvention() {
        classes()
            .that()
            .resideInAnyPackage("..domain.repository..")
            .should()
            .haveSimpleNameEndingWith("Repository")
            .check(importedClasses)
    }

    @Test
    fun everyEndpointDeclaresItsAccessPolicy() {
        classes()
            .that()
            .areAnnotatedWith(RestController::class.java)
            .should(
                archCondition("declare @AuthenticationRequired or @PublicEndpoint on every request mapping") { clazz: JavaClass, events ->
                    val classHasPolicy =
                        clazz.isAnnotatedWith(AuthenticationRequired::class.java) ||
                            clazz.isAnnotatedWith(PublicEndpoint::class.java)
                    if (classHasPolicy) return@archCondition
                    clazz.methods
                        .filter { method ->
                            method.annotations.any {
                                it.rawType.packageName == "org.springframework.web.bind.annotation" &&
                                    it.rawType.simpleName.endsWith("Mapping")
                            }
                        }.filterNot {
                            it.isAnnotatedWith(AuthenticationRequired::class.java) ||
                                it.isAnnotatedWith(PublicEndpoint::class.java)
                        }.forEach {
                            events.add(
                                SimpleConditionEvent.violated(
                                    it,
                                    "${it.fullName} does not declare its access policy",
                                ),
                            )
                        }
                },
            ).check(importedClasses)
    }

    @Test
    fun entitiesLiveInPersistenceModels() {
        classes()
            .that()
            .haveSimpleNameEndingWith("Entity")
            .should()
            .resideInAPackage("..infrastructure.persistence.models..")
            .check(importedClasses)
    }

    @Test
    fun domainModelsAreImmutable() {
        fields()
            .that()
            .areDeclaredInClassesThat(JavaClass.Predicates.resideInAPackage("..domain.models.."))
            .should()
            .beFinal()
            .check(importedClasses)
    }

    @Test
    fun controllersOnlyExposeDtos() {
        val domainPackage = Regex("""\.domain(\.|$)""")
        classes()
            .that()
            .resideInAPackage("..presentation.controller..")
            .should(
                archCondition("not expose domain types in their endpoints") { clazz: JavaClass, events ->
                    clazz.methods
                        .filter { JavaModifier.PUBLIC in it.modifiers }
                        .forEach { method ->
                            val exposed =
                                (listOf(method.rawReturnType) + method.rawParameterTypes)
                                    .filter { domainPackage.containsMatchIn(it.packageName) }
                            if (exposed.isNotEmpty()) {
                                events.add(
                                    SimpleConditionEvent.violated(
                                        method,
                                        "${method.fullName} exposes domain types: ${exposed.map { it.simpleName }}",
                                    ),
                                )
                            }
                        }
                },
            ).check(importedClasses)
    }

    @Test
    fun controllersDoNotImportDomain() {
        noClasses()
            .that()
            .resideInAPackage("..presentation.controller..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..domain..")
            .check(importedClasses)
    }

    @Test
    fun springBeansAreStateless() {
        fields()
            .that()
            .areDeclaredInClassesThat(
                annotatedWith(Service::class.java)
                    .or(annotatedWith(Component::class.java))
                    .or(annotatedWith(Repository::class.java))
                    .or(annotatedWith(RestController::class.java)),
            ).should()
            .beFinal()
            .check(importedClasses)
    }

    @Test
    fun loggingGoesThroughTheLoggerAbstraction() {
        noClasses()
            .that()
            .resideOutsideOfPackage("$ROOT.logger..")
            .should(ACCESS_STANDARD_STREAMS)
            .check(importedClasses)
    }

    @Test
    fun useCasesDoNotDependOnEachOther() {
        classes()
            .that()
            .resideInAPackage("..application.usecase..")
            .should(
                archCondition("not depend on other use cases") { clazz: JavaClass, events ->
                    clazz.directDependenciesFromSelf
                        .filter { it.targetClass != clazz && it.targetClass.packageName.contains(".application.usecase") }
                        .forEach { events.add(SimpleConditionEvent.violated(clazz, it.description)) }
                },
            ).check(importedClasses)
    }

    // ------------------------------------------------------------------
    // Clean Architecture : règle de dépendance
    // ------------------------------------------------------------------

    @Test
    fun layersFollowTheDependencyRule() {
        layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Domain")
            .definedBy("..domain..")
            .layer("Application")
            .definedBy("..application..")
            .layer("Infrastructure")
            .definedBy("..infrastructure..")
            .layer("Presentation")
            .definedBy("..presentation..")
            .whereLayer("Presentation")
            .mayNotBeAccessedByAnyLayer()
            .whereLayer("Infrastructure")
            .mayNotBeAccessedByAnyLayer()
            .whereLayer("Application")
            .mayOnlyBeAccessedByLayers("Infrastructure", "Presentation")
            .whereLayer("Domain")
            .mayOnlyBeAccessedByLayers("Application", "Infrastructure", "Presentation")
            .check(importedClasses)
    }

    @Test
    fun domainIsFrameworkFree() {
        noClasses()
            .that()
            .resideInAPackage("..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(*FRAMEWORK_PACKAGES, "$ROOT.logger..")
            .check(importedClasses)
    }

    @Test
    fun applicationIsFrameworkFree() {
        noClasses()
            .that()
            .resideInAPackage("..application..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(*FRAMEWORK_PACKAGES)
            .check(importedClasses)
    }

    @Test
    fun onlyCompositionRootsWireUseCases() {
        // L'infrastructure implémente des ports, elle n'appelle pas les use cases (sauf pour les câbler)
        noClasses()
            .that()
            .resideInAPackage("..infrastructure..")
            .and()
            .resideOutsideOfPackage("..infrastructure.config..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..application.usecase..")
            .check(importedClasses)
    }

    @Test
    fun presentationGoesThroughUseCasesNotThroughPorts() {
        noClasses()
            .that()
            .resideInAPackage("..presentation..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..domain.repository..")
            .check(importedClasses)
    }

    @Test
    fun persistenceInternalsDoNotLeak() {
        noClasses()
            .that()
            .resideOutsideOfPackage("..infrastructure.persistence..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                "..infrastructure.persistence.queries..",
                "..infrastructure.persistence.mapper..",
                "..infrastructure.persistence.models..",
            ).check(importedClasses)
    }

    @Test
    fun businessModulesAreIsolated() {
        // Un module métier ne dépend que de lui-même et de shared/common
        classes()
            .that()
            .resideInAPackage("$ROOT.modules..")
            .should(
                archCondition("only depend on their own module or on shared/common") { clazz: JavaClass, events ->
                    val own = moduleOf(clazz) ?: return@archCondition
                    clazz.directDependenciesFromSelf
                        .filter { dependency ->
                            val target = moduleOf(dependency.targetClass)
                            target != null && target != own && target !in SHARED_MODULES
                        }.forEach { events.add(SimpleConditionEvent.violated(clazz, it.description)) }
                },
            ).check(importedClasses)
    }

    // ------------------------------------------------------------------
    // D : Dependency Inversion (ports & adapters)
    // ------------------------------------------------------------------

    @Test
    fun portsAreImplementedInInfrastructure() {
        classes()
            .that()
            .implement(
                JavaClass.Predicates.resideInAnyPackage("..domain.repository..", "..application.service.."),
            ).should()
            .resideInAPackage("..infrastructure..")
            .check(importedClasses)
    }

    @Test
    fun repositoryAdaptersImplementADomainPort() {
        classes()
            .that()
            .areAnnotatedWith(Repository::class.java)
            .should()
            .implement(JavaClass.Predicates.resideInAPackage("..domain.repository.."))
            .check(importedClasses)
    }

    @Test
    fun useCasesDependOnAbstractionsOnly() {
        fields()
            .that()
            .areDeclaredInClassesThat(JavaClass.Predicates.resideInAPackage("..application.usecase.."))
            .should(
                archCondition("have an interface type (abstraction)") { field: JavaField, events ->
                    val type = field.rawType
                    val isAllowed =
                        type.isInterface ||
                            type.isPrimitive ||
                            type.packageName.startsWith("java") ||
                            type.packageName.startsWith("kotlin")
                    if (!isAllowed) {
                        events.add(
                            SimpleConditionEvent.violated(
                                field,
                                "${field.fullName} has concrete type ${type.name}",
                            ),
                        )
                    }
                },
            ).check(importedClasses)
    }

    @Test
    fun noFieldInjection() {
        noFields()
            .should()
            .beAnnotatedWith(Autowired::class.java)
            .orShould()
            .beAnnotatedWith("jakarta.inject.Inject")
            .orShould()
            .beAnnotatedWith("jakarta.annotation.Resource")
            .check(importedClasses)
    }

    // ------------------------------------------------------------------
    // S et I : Single Responsibility / Interface Segregation (heuristiques)
    // ------------------------------------------------------------------

    @Test
    fun useCasesExposeASinglePublicMethod() {
        classes()
            .that()
            .resideInAPackage("..application.usecase..")
            .should(
                archCondition("expose exactly one public method (invoke)") { clazz: JavaClass, events ->
                    val publicMethods = clazz.methods.filter { JavaModifier.PUBLIC in it.modifiers }
                    if (publicMethods.size != 1) {
                        events.add(
                            SimpleConditionEvent.violated(
                                clazz,
                                "${clazz.name} exposes ${publicMethods.size} public methods: ${publicMethods.map { it.name }}",
                            ),
                        )
                    }
                },
            ).check(importedClasses)
    }

    @Test
    fun portsAreSmall() {
        classes()
            .that()
            .areInterfaces()
            .and()
            .resideInAnyPackage("..domain.repository..", "..application.service..")
            .should(
                archCondition("declare at most $MAX_PORT_METHODS methods") { clazz: JavaClass, events ->
                    if (clazz.methods.size > MAX_PORT_METHODS) {
                        events.add(
                            SimpleConditionEvent.violated(
                                clazz,
                                "${clazz.name} declares ${clazz.methods.size} methods, split it (ISP)",
                            ),
                        )
                    }
                },
            ).check(importedClasses)
    }

    @Test
    fun componentsDoNotHaveTooManyDependencies() {
        classes()
            .that()
            .resideInAnyPackage(
                "..application.usecase..",
                "..presentation.controller..",
                "..infrastructure.persistence.repository..",
            ).should(
                archCondition("have at most $MAX_CONSTRUCTOR_DEPENDENCIES constructor dependencies") { clazz: JavaClass, events ->
                    val count =
                        clazz.constructors
                            .filterNot { JavaModifier.SYNTHETIC in it.modifiers }
                            .maxOfOrNull { it.rawParameterTypes.size } ?: 0
                    if (count > MAX_CONSTRUCTOR_DEPENDENCIES) {
                        events.add(
                            SimpleConditionEvent.violated(
                                clazz,
                                "${clazz.name} has $count constructor parameters, it probably does too much (SRP)",
                            ),
                        )
                    }
                },
            ).check(importedClasses)
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private fun moduleOf(clazz: JavaClass): String? = MODULE_NAME.find(clazz.name)?.groupValues?.get(1)

    private fun <T> archCondition(
        description: String,
        block: (T, ConditionEvents) -> Unit,
    ): ArchCondition<T> =
        object : ArchCondition<T>(description) {
            override fun check(
                item: T,
                events: ConditionEvents,
            ) = block(item, events)
        }
}
