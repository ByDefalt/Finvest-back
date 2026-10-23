package defalt.arch

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaField
import com.tngtech.archunit.core.domain.JavaModifier
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.Locations
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.GeneralCodingRules.ACCESS_STANDARD_STREAMS
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING
import defalt.arch.Layer.API
import defalt.arch.Layer.APP
import defalt.arch.Layer.APPLICATION
import defalt.arch.Layer.DOMAIN
import defalt.arch.Layer.INFRASTRUCTURE
import defalt.arch.Layer.PRESENTATION
import defalt.arch.Layer.UTILS
import defalt.exception.CustomException
import defalt.exception.ErrorCode
import defalt.logger.Logger
import defalt.manager.TransactionManager
import defalt.security.AuthenticationRequired
import defalt.security.PublicEndpoint
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Architecture rules of Finvest. The layer of a class is the Gradle module it is compiled in
 * (see [ArchitectureSupport][moduleLayerOf]): `modules/<module>/<layer>`, `core/<layer>` or `app`.
 */
class MyArchitectureTest {
    private companion object {
        // Ports: the largest one today is AccountRepository (CRUD + 3 queries). Split a port before growing it.
        const val MAX_PORT_METHODS = 7

        // Business collaborators only: Logger and TransactionManager are technical and not counted
        const val MAX_CONSTRUCTOR_DEPENDENCIES = 5
        val TECHNICAL_DEPENDENCIES = setOf(Logger::class.java.name, TransactionManager::class.java.name)

        val FRAMEWORK_PACKAGES =
            arrayOf(
                "org.springframework..",
                "jakarta..",
                "javax..",
                "java.sql..",
                "tools.jackson..",
                "com.fasterxml..",
                "io.jsonwebtoken..",
            )

        /** Layers each layer may depend on (in its own module, in core, or in the api of another module). */
        val ALLOWED_LAYERS =
            mapOf(
                API to setOf(API, DOMAIN),
                DOMAIN to setOf(DOMAIN, API),
                APPLICATION to setOf(DOMAIN, APPLICATION, API, UTILS),
                INFRASTRUCTURE to setOf(DOMAIN, APPLICATION, INFRASTRUCTURE, API, UTILS),
                PRESENTATION to setOf(DOMAIN, APPLICATION, PRESENTATION, API, UTILS),
                UTILS to setOf(UTILS),
            )

        val classes get() = PROJECT_CLASSES

        fun beInLayer(vararg layers: Layer): ArchCondition<JavaClass> =
            archCondition("be in layer ${layers.joinToString(" or ") { it.name.lowercase() }}") { clazz, events ->
                if (clazz.moduleLayer?.layer !in layers) events.violation(clazz, "${clazz.name} is in ${clazz.moduleLayer}")
            }

        fun topLevelClassesInPackage(packageName: String) =
            TOP_LEVEL_PROJECT_CLASSES.and(
                described("reside in package $packageName") { it.packageName == packageName },
            )
    }

    // ------------------------------------------------------------------
    // Preconditions: without them every other rule would pass on an empty set
    // ------------------------------------------------------------------

    @Nested
    inner class Preconditions {
        @Test
        fun `every production class is located in a known module layer`() {
            val unknown = classes.filter { it.moduleLayer == null }.map { "${it.name} (${it.source.orElse(null)?.uri})" }

            assertTrue(unknown.isEmpty(), "Classes outside of any module layer:\n${unknown.joinToString("\n")}")
        }

        @Test
        fun `every module layer is analysed`() {
            val expected =
                listOf(DOMAIN, APPLICATION, INFRASTRUCTURE, PRESENTATION, UTILS).map { ModuleLayer(CORE_MODULE, it) } +
                    listOf(API, DOMAIN, APPLICATION, INFRASTRUCTURE, PRESENTATION).map { ModuleLayer("auth", it) } +
                    listOf(DOMAIN, APPLICATION, INFRASTRUCTURE, PRESENTATION).map { ModuleLayer("account", it) } +
                    ModuleLayer(APP_MODULE, APP)

            assertEquals(expected.toSet(), classes.mapNotNull { it.moduleLayer }.toSet())
        }

        @Test
        fun `no class is defined in two modules`() {
            // Two modules defining the same class name: only one of them is loaded at runtime, depending on the classpath order
            val modulesByClass =
                Locations
                    .ofPackage(ROOT_PACKAGE)
                    .filter { location -> PRODUCTION_CODE.all { it.includes(location) } }
                    .flatMap { location ->
                        ClassFileImporter()
                            .importLocations(listOf(location))
                            .filter { it.isTopLevelClass }
                            .map { it.name to moduleLayerOf(location.asURI().toString()) }
                    }.groupBy({ it.first }, { it.second })
                    .mapValues { it.value.toSet() }

            val duplicates = modulesByClass.filterValues { it.size > 1 }

            assertTrue(duplicates.isEmpty(), "Classes defined in several modules:\n${duplicates.entries.joinToString("\n")}")
        }
    }

    // ------------------------------------------------------------------
    // Clean Architecture: dependency rule between layers and modules
    // ------------------------------------------------------------------

    @Nested
    inner class DependencyRule {
        @Test
        fun `layers only depend on inner layers`() {
            classes()
                .that(TOP_LEVEL_PROJECT_CLASSES.and(inLayer(*Layer.entries.filter { it != APP }.toTypedArray())))
                .should(
                    archCondition("only depend on the layers they are allowed to") { clazz: JavaClass, events ->
                        val source = clazz.moduleLayer!!
                        clazz.directDependenciesFromSelf
                            .filter { it.targetClass.moduleLayer != null }
                            .filterNot { dependency ->
                                val target = dependency.targetClass.moduleLayer!!
                                val allowed = target.layer in ALLOWED_LAYERS.getValue(source.layer)
                                // An api exposes only its own types and core value objects
                                allowed && (source.layer != API || target.layer == API || target.isCore)
                            }.forEach { events.violation(clazz, "$source -> ${it.targetClass.moduleLayer}: ${it.description}") }
                    },
                ).check(classes)
        }

        @Test
        fun `core does not depend on business modules`() {
            classes()
                .that(TOP_LEVEL_PROJECT_CLASSES.and(described("are in core") { it.moduleLayer?.isCore == true }))
                .should(
                    archCondition("only depend on core") { clazz: JavaClass, events ->
                        clazz.directDependenciesFromSelf
                            .filter { it.targetClass.moduleLayer?.let { target -> !target.isCore } == true }
                            .forEach { events.violation(clazz, it.description) }
                    },
                ).check(classes)
        }

        @Test
        fun `business modules only talk to each other through their api`() {
            classes()
                .that(
                    TOP_LEVEL_PROJECT_CLASSES.and(
                        described("are in a business module") {
                            it.moduleLayer.let { layer -> layer != null && !layer.isCore && layer.layer != APP }
                        },
                    ),
                ).should(
                    archCondition("only depend on their own module, on core or on the api of another module") { clazz: JavaClass, events ->
                        val source = clazz.moduleLayer!!
                        clazz.directDependenciesFromSelf
                            .filter { dependency ->
                                val target = dependency.targetClass.moduleLayer ?: return@filter false
                                target.module != source.module && !target.isCore && target.layer != API
                            }.forEach { events.violation(clazz, it.description) }
                    },
                ).check(classes)
        }

        @Test
        fun `nothing depends on the application module`() {
            noClasses()
                .that(TOP_LEVEL_PROJECT_CLASSES.and(inLayer(APP).negate()))
                .should()
                .dependOnClassesThat(inLayer(APP))
                .check(classes)
        }

        @Test
        fun `presentation reaches the application through use cases, commands and service ports`() {
            noClasses()
                .that(inLayer(PRESENTATION))
                .should()
                .dependOnClassesThat(
                    inPackageOfLayer("defalt.repository", DOMAIN)
                        .or(inPackageOfLayer("defalt.solver", APPLICATION))
                        .or(inPackageOfLayer("defalt.resolver", APPLICATION)),
                ).check(classes)
        }

        @Test
        fun `infrastructure only references use cases to wire them`() {
            noClasses()
                .that(inLayer(INFRASTRUCTURE).and(described("are not in defalt.config") { it.packageName != "defalt.config" }))
                .should()
                .dependOnClassesThat(inPackageOfLayer("defalt.usecase", APPLICATION))
                .check(classes)
        }

        @Test
        fun `use cases are only instantiated by the composition roots`() {
            classes()
                .that(TOP_LEVEL_PROJECT_CLASSES)
                .and()
                .resideOutsideOfPackage("defalt.config")
                .should(
                    archCondition("not instantiate use cases") { clazz: JavaClass, events ->
                        clazz.constructorCallsFromSelf
                            .filter { it.targetOwner.packageName == "defalt.usecase" && it.targetOwner != clazz }
                            .forEach { events.violation(clazz, it.description) }
                    },
                ).check(classes)
        }
    }

    // ------------------------------------------------------------------
    // Framework isolation
    // ------------------------------------------------------------------

    @Nested
    inner class FrameworkIsolation {
        @Test
        fun `domain and apis are framework free`() {
            noClasses()
                .that(inLayer(DOMAIN, API))
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(*FRAMEWORK_PACKAGES)
                .check(classes)
        }

        @Test
        fun `application is framework free`() {
            noClasses()
                .that(inLayer(APPLICATION))
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(*FRAMEWORK_PACKAGES)
                .check(classes)
        }

        @Test
        fun `transactions are only handled by the core transaction manager`() {
            noClasses()
                .that(inCore(INFRASTRUCTURE).negate())
                .should()
                .dependOnClassesThat()
                .resideInAPackage("org.springframework.transaction..")
                .check(classes)
        }

        @Test
        fun `jdbc is only used by the infrastructure`() {
            noClasses()
                .that(inLayer(INFRASTRUCTURE).negate())
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("org.springframework.jdbc..", "java.sql..", "javax.sql..")
                .check(classes)
        }

        @Test
        fun `http is only handled by the presentation`() {
            noClasses()
                .that(inLayer(PRESENTATION, APP).negate())
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("org.springframework.web..", "org.springframework.http..", "jakarta.servlet..")
                .check(classes)
        }

        @Test
        fun `json is only handled by the presentation`() {
            noClasses()
                .that(inLayer(PRESENTATION, APP).negate())
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("tools.jackson..", "com.fasterxml.jackson..")
                .check(classes)
        }
    }

    // ------------------------------------------------------------------
    // Placement and naming conventions
    // ------------------------------------------------------------------

    @Nested
    inner class Conventions {
        @Test
        fun `controllers are presentation classes of defalt controller`() {
            classes()
                .that()
                .areAnnotatedWith(RestController::class.java)
                .should(beInLayer(PRESENTATION))
                .andShould()
                .resideInAPackage("defalt.controller")
                .andShould()
                .haveSimpleNameEndingWith("Controller")
                .check(classes)
        }

        @Test
        fun `defalt controller only contains controllers`() {
            classes()
                .that(
                    topLevelClassesInPackage("defalt.controller"),
                ).should()
                .beAnnotatedWith(RestController::class.java)
                .check(classes)
        }

        @Test
        fun `exception handlers live in core presentation`() {
            classes()
                .that()
                .areAnnotatedWith(RestControllerAdvice::class.java)
                .should(beInLayer(PRESENTATION))
                .andShould()
                .resideInAPackage("defalt.exception")
                .andShould()
                .haveSimpleNameEndingWith("ExceptionHandler")
                .check(classes)
        }

        @Test
        fun `configurations are composition roots of defalt config`() {
            classes()
                .that()
                .areAnnotatedWith(Configuration::class.java)
                .should(beInLayer(INFRASTRUCTURE, APP))
                .andShould()
                .resideInAPackage("defalt.config")
                .andShould()
                .haveSimpleNameEndingWith("Config")
                .check(classes)
        }

        @Test
        fun `defalt config only contains configurations`() {
            classes()
                .that(topLevelClassesInPackage("defalt.config"))
                .should()
                .beAnnotatedWith(Configuration::class.java)
                .check(classes)
        }

        @Test
        fun `repository adapters are jdbc repositories implementing a domain port`() {
            classes()
                .that()
                .areAnnotatedWith(Repository::class.java)
                .should(beInLayer(INFRASTRUCTURE))
                .andShould()
                .resideInAPackage("defalt.persistence.repository")
                .andShould()
                .haveSimpleNameEndingWith("JdbcRepository")
                .andShould()
                .implement(inPackageOfLayer("defalt.repository", DOMAIN))
                .check(classes)
        }

        @Test
        fun `defalt persistence repository only contains repository adapters`() {
            classes()
                .that(topLevelClassesInPackage("defalt.persistence.repository"))
                .should()
                .beAnnotatedWith(Repository::class.java)
                .check(classes)
        }

        @Test
        fun `sql queries are only used by the repository adapters`() {
            classes()
                .that(topLevelClassesInPackage("defalt.persistence.queries"))
                .should(beInLayer(INFRASTRUCTURE))
                .andShould()
                .haveSimpleNameEndingWith("Queries")
                .andShould()
                .onlyHaveDependentClassesThat()
                .resideInAnyPackage("defalt.persistence.queries", "defalt.persistence.repository")
                .check(classes)
        }

        @Test
        fun `domain ports are repository interfaces`() {
            classes()
                .that(topLevelClassesInPackage("defalt.repository"))
                .should(beInLayer(DOMAIN))
                .andShould()
                .beInterfaces()
                .andShould()
                .haveSimpleNameEndingWith("Repository")
                .check(classes)
        }

        @Test
        fun `application services are ports`() {
            classes()
                .that(topLevelClassesInPackage("defalt.service").and(inLayer(APPLICATION)))
                .should()
                .beInterfaces()
                .check(classes)
        }

        @Test
        fun `use cases are final application classes named UseCase`() {
            classes()
                .that(topLevelClassesInPackage("defalt.usecase"))
                .should(beInLayer(APPLICATION))
                .andShould()
                .haveSimpleNameEndingWith("UseCase")
                .andShould()
                .haveModifier(JavaModifier.FINAL)
                .check(classes)
        }

        @Test
        fun `commands are application classes named Command`() {
            classes()
                .that(topLevelClassesInPackage("defalt.command"))
                .should(beInLayer(APPLICATION))
                .andShould()
                .haveSimpleNameEndingWith("Command")
                .check(classes)
        }

        @Test
        fun `policies are domain classes and solvers and resolvers are application classes`() {
            classes().that(topLevelClassesInPackage("defalt.policy")).should(beInLayer(DOMAIN)).check(classes)
            classes()
                .that(topLevelClassesInPackage("defalt.solver").or(topLevelClassesInPackage("defalt.resolver")))
                .should(beInLayer(APPLICATION))
                .check(classes)
        }

        @Test
        fun `value objects belong to a domain or an api`() {
            classes().that(topLevelClassesInPackage("defalt.valueobject")).should(beInLayer(DOMAIN, API)).check(classes)
        }

        @Test
        fun `error codes are domain enums`() {
            classes()
                .that(TOP_LEVEL_PROJECT_CLASSES)
                .and()
                .implement(ErrorCode::class.java)
                .should(beInLayer(DOMAIN))
                .andShould()
                .beEnums()
                .andShould()
                .resideInAPackage("defalt.exception")
                .andShould()
                .haveSimpleNameEndingWith("ErrorCode")
                .check(classes)
        }

        @Test
        fun `requests, responses, dtos and mappers belong to the presentation`() {
            classes()
                .that(TOP_LEVEL_PROJECT_CLASSES)
                .and()
                .haveNameMatching(""".*(Request|Response|Dto)""")
                .should(beInLayer(PRESENTATION))
                .check(classes)
            classes().that(topLevelClassesInPackage("defalt.mapper")).should(beInLayer(PRESENTATION)).check(classes)
        }

        @Test
        fun `every endpoint declares its access policy`() {
            classes()
                .that()
                .areAnnotatedWith(RestController::class.java)
                .should(
                    archCondition(
                        "declare @AuthenticationRequired or @PublicEndpoint on every request mapping",
                    ) { clazz: JavaClass, events ->
                        val classHasPolicy =
                            clazz.isAnnotatedWith(AuthenticationRequired::class.java) || clazz.isAnnotatedWith(PublicEndpoint::class.java)
                        if (classHasPolicy) return@archCondition
                        clazz.methods
                            .filter { method ->
                                method.annotations.any {
                                    it.rawType.packageName == "org.springframework.web.bind.annotation" &&
                                        it.rawType.simpleName.endsWith("Mapping")
                                }
                            }.filterNot {
                                it.isAnnotatedWith(AuthenticationRequired::class.java) || it.isAnnotatedWith(PublicEndpoint::class.java)
                            }.forEach { events.violation(it, "${it.fullName} does not declare its access policy") }
                    },
                ).check(classes)
        }

        @Test
        fun `controllers only expose dtos`() {
            classes()
                .that()
                .areAnnotatedWith(RestController::class.java)
                .should(
                    archCondition("not expose domain types in their endpoints") { clazz: JavaClass, events ->
                        clazz.methods
                            .filter { JavaModifier.PUBLIC in it.modifiers }
                            .forEach { method ->
                                val exposed =
                                    (listOf(method.rawReturnType) + method.rawParameterTypes).filter {
                                        it.moduleLayer?.layer ==
                                            DOMAIN
                                    }
                                if (exposed.isNotEmpty()) {
                                    events.violation(method, "${method.fullName} exposes domain types: ${exposed.map { it.simpleName }}")
                                }
                            }
                    },
                ).check(classes)
        }
    }

    // ------------------------------------------------------------------
    // Immutability and statelessness
    // ------------------------------------------------------------------

    @Nested
    inner class Immutability {
        @Test
        fun `domain models, value objects and policies are immutable`() {
            fields()
                .that()
                .areDeclaredInClassesThat(
                    inPackageOfLayer("defalt.models", DOMAIN)
                        .or(inPackageOfLayer("defalt.valueobject", DOMAIN, API))
                        .or(inPackageOfLayer("defalt.policy", DOMAIN)),
                ).should()
                .beFinal()
                .check(classes)
        }

        @Test
        fun `commands, requests, responses and dtos are immutable`() {
            fields()
                .that()
                .areDeclaredInClassesThat(
                    inPackageOfLayer("defalt.command", APPLICATION)
                        .or(
                            inLayer(
                                PRESENTATION,
                            ).and(
                                described("are requests, responses or dtos") { it.simpleName.matches(Regex(".*(Request|Response|Dto)")) },
                            ),
                        ),
                ).should()
                .beFinal()
                .check(classes)
        }

        @Test
        fun `use cases are stateless`() {
            fields()
                .that()
                .areDeclaredInClassesThat(inPackageOfLayer("defalt.usecase", APPLICATION))
                .should()
                .beFinal()
                .andShould()
                .bePrivate()
                .check(classes)
        }

        @Test
        fun `spring beans are stateless`() {
            fields()
                .that()
                .areDeclaredInClassesThat(
                    annotatedWith(Service::class.java)
                        .or(annotatedWith(Component::class.java))
                        .or(annotatedWith(Repository::class.java))
                        .or(annotatedWith(RestController::class.java))
                        .or(annotatedWith(RestControllerAdvice::class.java))
                        .or(annotatedWith(Configuration::class.java)),
                ).should()
                .beFinal()
                .check(classes)
        }
    }

    // ------------------------------------------------------------------
    // SOLID heuristics
    // ------------------------------------------------------------------

    @Nested
    inner class Solid {
        private val ports =
            inPackageOfLayer("defalt.repository", DOMAIN)
                .or(inPackageOfLayer("defalt.service", APPLICATION))
                .or(inLayer(API))

        @Test
        fun `S - use cases expose a single invoke method`() {
            classes()
                .that(topLevelClassesInPackage("defalt.usecase"))
                .should(
                    archCondition("expose exactly one public method, invoke") { clazz: JavaClass, events ->
                        val publicMethods =
                            clazz.methods.filter {
                                JavaModifier.PUBLIC in it.modifiers &&
                                    JavaModifier.SYNTHETIC !in it.modifiers
                            }
                        // Kotlin mangles the name of functions taking value classes: invoke-<hash>
                        if (publicMethods.size != 1 || !publicMethods.single().name.startsWith("invoke")) {
                            events.violation(clazz, "${clazz.name} exposes ${publicMethods.map { it.name }}")
                        }
                    },
                ).check(classes)
        }

        @Test
        fun `S - use cases do not depend on each other`() {
            classes()
                .that(topLevelClassesInPackage("defalt.usecase"))
                .should(
                    archCondition("not depend on other use cases") { clazz: JavaClass, events ->
                        clazz.directDependenciesFromSelf
                            .filter { it.targetClass != clazz && it.targetClass.packageName == "defalt.usecase" }
                            .forEach { events.violation(clazz, it.description) }
                    },
                ).check(classes)
        }

        @Test
        fun `S - components do not have too many business dependencies`() {
            classes()
                .that(
                    topLevelClassesInPackage("defalt.usecase")
                        .or(annotatedWith(RestController::class.java))
                        .or(annotatedWith(Repository::class.java)),
                ).should(
                    archCondition(
                        "have at most $MAX_CONSTRUCTOR_DEPENDENCIES business constructor dependencies",
                    ) { clazz: JavaClass, events ->
                        val dependencies = clazz.constructorParameterTypes().filterNot { it.name in TECHNICAL_DEPENDENCIES }
                        if (dependencies.size > MAX_CONSTRUCTOR_DEPENDENCIES) {
                            events.violation(
                                clazz,
                                "${clazz.name} has ${dependencies.size} business dependencies, it probably does too much",
                            )
                        }
                    },
                ).check(classes)
        }

        @Test
        fun `I - ports are small`() {
            classes()
                .that(TOP_LEVEL_PROJECT_CLASSES.and(ports))
                .and()
                .areInterfaces()
                .should(
                    archCondition("declare at most $MAX_PORT_METHODS methods") { clazz: JavaClass, events ->
                        if (clazz.methods.size > MAX_PORT_METHODS) {
                            events.violation(clazz, "${clazz.name} declares ${clazz.methods.size} methods, split it")
                        }
                    },
                ).check(classes)
        }

        @Test
        fun `D - ports are implemented by the infrastructure`() {
            classes()
                .that(TOP_LEVEL_PROJECT_CLASSES)
                .and()
                .implement(ports.and(described("are interfaces") { it.isInterface }))
                .should(beInLayer(INFRASTRUCTURE))
                .check(classes)
        }

        @Test
        fun `D - use cases depend on abstractions or on application classes`() {
            fields()
                .that()
                .areDeclaredInClassesThat(inPackageOfLayer("defalt.usecase", APPLICATION))
                .should(
                    archCondition("have an interface, an application or a standard library type") { field: JavaField, events ->
                        val type = field.rawType
                        val isStandard = type.isPrimitive || type.packageName.startsWith("java.") || type.packageName.startsWith("kotlin")
                        if (!type.isInterface && !isStandard && type.moduleLayer?.layer != APPLICATION) {
                            events.violation(field, "${field.fullName} has the concrete type ${type.name}")
                        }
                    },
                ).check(classes)
        }

        @Test
        fun `D - dependencies are injected through constructors`() {
            // A final field can only be set by the constructor. Kotlin may copy a constructor parameter
            // annotation (@Value...) on the backing field: that is still constructor injection.
            fields()
                .that()
                .areAnnotatedWith(Autowired::class.java)
                .or()
                .areAnnotatedWith(Value::class.java)
                .or()
                .areAnnotatedWith("jakarta.inject.Inject")
                .or()
                .areAnnotatedWith("jakarta.annotation.Resource")
                .should()
                .beFinal()
                .allowEmptyShould(true)
                .check(classes)
            noClasses()
                .should(
                    archCondition("not use setter injection") { clazz: JavaClass, events ->
                        clazz.methods
                            .filter { it.isAnnotatedWith(Autowired::class.java) || it.isAnnotatedWith("jakarta.inject.Inject") }
                            .forEach { events.violation(it, "${it.fullName} is an injection method") }
                    },
                ).check(classes)
        }
    }

    // ------------------------------------------------------------------
    // Coding rules
    // ------------------------------------------------------------------

    @Nested
    inner class CodingRules {
        @Test
        fun `logging goes through the Logger abstraction`() {
            noClasses()
                .that(inCore(UTILS).negate())
                .should(ACCESS_STANDARD_STREAMS)
                .orShould()
                .dependOnClassesThat()
                .haveFullyQualifiedName("kotlin.io.ConsoleKt")
                .orShould()
                .dependOnClassesThat()
                .resideInAnyPackage("org.slf4j..", "org.apache.logging..", "ch.qos.logback..")
                .check(classes)
            NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING.check(classes)
        }

        @Test
        fun `no generic exception is thrown`() {
            NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS.check(classes)
        }

        @Test
        fun `errors are raised as CustomException with a translatable code`() {
            // Also catches error(), check(), require(), TODO()... that Kotlin inlines in the calling class.
            // Ignored: the exceptions generated by the compiler itself, never written in the code
            // (exhaustive when on enums / sealed types, synthetic $default methods).
            val compilerGenerated = setOf(NoWhenBranchMatchedException::class.java.name)
            classes()
                .that(
                    described("are project classes other than CustomException") {
                        it.moduleLayer != null && it.name != CustomException::class.java.name
                    },
                ).should(
                    archCondition("only instantiate CustomException as exception") { clazz: JavaClass, events ->
                        clazz.constructorCallsFromSelf
                            .filter { it.targetOwner.isAssignableTo(Throwable::class.java) }
                            .filterNot { it.targetOwner.isAssignableTo(CustomException::class.java) }
                            .filterNot { it.targetOwner.name in compilerGenerated }
                            .filterNot { JavaModifier.SYNTHETIC in it.origin.modifiers }
                            .forEach { events.violation(clazz, it.description) }
                    },
                ).check(classes)
        }
    }
}
