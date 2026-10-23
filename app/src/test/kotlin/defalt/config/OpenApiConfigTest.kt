package defalt.config

import defalt.models.AuthenticatedUser
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.parameters.Parameter
import org.junit.jupiter.api.Test
import org.springframework.web.method.HandlerMethod
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Edge cases of the OpenAPI customizers; the nominal case is covered by ApplicationConfigIntegrationTest. */
class OpenApiConfigTest {
    private val config = OpenApiConfig()

    @Test
    fun `tag customizer accepts a document without paths and operations without tags`() {
        val withoutTags = Operation()
        val openApi = OpenAPI().paths(Paths().addPathItem("/x", PathItem().get(withoutTags)))

        config.customizeControllerTags().customise(OpenAPI())
        config.customizeControllerTags().customise(openApi)

        assertNull(withoutTags.tags)
    }

    @Test
    fun `tag customizer removes the controller suffix`() {
        val operation = Operation().tags(listOf("account-controller", "other"))

        config.customizeControllerTags().customise(OpenAPI().paths(Paths().addPathItem("/x", PathItem().get(operation))))

        assertEquals(listOf("account", "other"), operation.tags)
    }

    @Test
    fun `operation customizer only removes the authenticated user parameter of endpoints receiving it`() {
        val customizer = config.authenticatedUserOperationCustomizer()
        val withUser = Operation().parameters(mutableListOf(Parameter().name("authenticatedUser"), Parameter().name("id")))
        val withoutUser = Operation().parameters(mutableListOf(Parameter().name("authenticatedUser")))

        customizer.customize(withUser, handler("withUser"))
        customizer.customize(withoutUser, handler("withoutUser"))
        customizer.customize(Operation(), handler("withUser"))

        assertEquals(listOf("id"), withUser.parameters.map { it.name })
        assertEquals(listOf("authenticatedUser"), withoutUser.parameters.map { it.name })
    }

    private fun handler(name: String) = HandlerMethod(Endpoints(), Endpoints::class.java.methods.single { it.name == name })

    @Suppress("unused", "UNUSED_PARAMETER")
    class Endpoints {
        fun withUser(
            user: AuthenticatedUser,
            id: Long,
        ) = Unit

        fun withoutUser(id: Long) = Unit
    }
}
