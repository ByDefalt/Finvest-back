package com.example.finvest.config

import com.example.finvest.modules.shared.application.models.AuthenticatedUser
import org.springdoc.core.customizers.GlobalOpenApiCustomizer
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun customizeControllerTags(): GlobalOpenApiCustomizer =
        GlobalOpenApiCustomizer { openApi ->

            openApi.paths?.values?.forEach { pathItem ->

                pathItem.readOperations().forEach { operation ->

                    operation.tags =
                        operation.tags
                            ?.map { tag ->
                                tag.removeSuffix("-controller")
                            }
                }
            }
        }

    @Bean
    fun authenticatedUserOperationCustomizer(): OperationCustomizer =
        OperationCustomizer { operation, handlerMethod ->

            val hasCurrentUser =
                handlerMethod.methodParameters.any {
                    it.parameterType == AuthenticatedUser::class.java
                }

            if (hasCurrentUser) {
                operation.parameters?.removeIf { parameter ->
                    parameter.name == "authenticatedUser"
                }
            }

            operation
        }
}
