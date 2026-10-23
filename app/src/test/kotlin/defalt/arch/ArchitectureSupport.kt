package defalt.arch

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.domain.JavaModifier
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.Location
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent

/**
 * Every package of the project lives under `defalt` and packages are shared between Gradle modules
 * (`defalt.usecase` exists in auth and account...). The layer of a class is therefore given by the
 * Gradle module it is compiled in, read from the location of its class file:
 *
 * - `modules/<module>/<layer>/build/...` : business module, layer api | domain | application | infrastructure | presentation
 * - `core/<layer>/build/...`             : shared kernel, layer domain | application | infrastructure | presentation | utils
 * - `app/build/...`                      : Spring Boot application (composition root)
 */
enum class Layer { API, DOMAIN, APPLICATION, INFRASTRUCTURE, PRESENTATION, UTILS, APP }

data class ModuleLayer(
    val module: String,
    val layer: Layer,
) {
    val isCore: Boolean get() = module == CORE_MODULE

    override fun toString() = if (layer == Layer.APP) APP_MODULE else "$module:${layer.name.lowercase()}"
}

const val ROOT_PACKAGE = "defalt"
const val CORE_MODULE = "core"
const val APP_MODULE = "app"

private val BUSINESS_MODULE_LOCATION = Regex("""/modules/([^/]+)/([^/]+)/build/""")
private val CORE_MODULE_LOCATION = Regex("""/core/([^/]+)/build/""")
private val APP_MODULE_LOCATION = Regex("""/app/build/""")

fun moduleLayerOf(location: String): ModuleLayer? {
    BUSINESS_MODULE_LOCATION.find(location)?.let { match ->
        return layerNamed(match.groupValues[2])?.let { ModuleLayer(match.groupValues[1], it) }
    }
    CORE_MODULE_LOCATION.find(location)?.let { match ->
        return layerNamed(match.groupValues[1])?.let { ModuleLayer(CORE_MODULE, it) }
    }
    return if (APP_MODULE_LOCATION.containsMatchIn(location)) ModuleLayer(APP_MODULE, Layer.APP) else null
}

private fun layerNamed(name: String): Layer? = Layer.entries.find { it.name.equals(name, ignoreCase = true) && it != Layer.APP }

/** Module and layer of a project class, null for JDK / library classes. */
val JavaClass.moduleLayer: ModuleLayer?
    get() =
        source
            .orElse(null)
            ?.uri
            ?.toString()
            ?.let(::moduleLayerOf)

/** Test fixtures jars / directories are test code, even though they are not under `src/test`. */
object DoNotIncludeTestFixtures : ImportOption {
    override fun includes(location: Location) = !location.contains("test-fixtures") && !location.contains("/testFixtures/")
}

val PRODUCTION_CODE: List<ImportOption> = listOf(ImportOption.DoNotIncludeTests(), DoNotIncludeTestFixtures)

/** Production classes of every module, imported once for all the architecture tests. */
val PROJECT_CLASSES: JavaClasses by lazy {
    ClassFileImporter().withImportOptions(PRODUCTION_CODE).importPackages(ROOT_PACKAGE)
}

// ------------------------------------------------------------------
// Predicates
// ------------------------------------------------------------------

fun described(
    description: String,
    test: (JavaClass) -> Boolean,
): DescribedPredicate<JavaClass> = DescribedPredicate.describe(description, test)

fun inLayer(vararg layers: Layer): DescribedPredicate<JavaClass> =
    DescribedPredicate.describe("are in layer ${layers.joinToString(" or ") { it.name.lowercase() }}") {
        it.moduleLayer?.layer in layers
    }

fun inCore(layer: Layer): DescribedPredicate<JavaClass> =
    DescribedPredicate.describe("are in core:${layer.name.lowercase()}") {
        it.moduleLayer == ModuleLayer(CORE_MODULE, layer)
    }

fun inPackageOfLayer(
    packageName: String,
    vararg layers: Layer,
): DescribedPredicate<JavaClass> =
    DescribedPredicate.describe("are in package $packageName of layer ${layers.joinToString(" or ") { it.name.lowercase() }}") {
        it.packageName == packageName && it.moduleLayer?.layer in layers
    }

val TOP_LEVEL_PROJECT_CLASSES: DescribedPredicate<JavaClass> =
    DescribedPredicate.describe("are top level project classes") { it.isTopLevelClass && it.moduleLayer != null }

// ------------------------------------------------------------------
// Conditions
// ------------------------------------------------------------------

fun <T> archCondition(
    description: String,
    block: (T, ConditionEvents) -> Unit,
): ArchCondition<T> =
    object : ArchCondition<T>(description) {
        override fun check(
            item: T,
            events: ConditionEvents,
        ) = block(item, events)
    }

fun ConditionEvents.violation(
    item: Any,
    message: String,
) = add(SimpleConditionEvent.violated(item, message))

/** Constructor parameters of [this], ignoring the synthetic constructors generated by Kotlin. */
fun JavaClass.constructorParameterTypes(): List<JavaClass> =
    constructors
        .filterNot { JavaModifier.SYNTHETIC in it.modifiers }
        .maxByOrNull { it.rawParameterTypes.size }
        ?.rawParameterTypes
        .orEmpty()
