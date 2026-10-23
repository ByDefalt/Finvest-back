package defalt.mapper

import org.springframework.stereotype.Component
import tools.jackson.databind.jsontype.NamedType
import tools.jackson.databind.module.SimpleModule

/**
 * Registers each details DTO under its account type code, so that Jackson can pick the
 * concrete [defalt.models.dto.AccountDetailsDto] from the request's `accountTypeCode`.
 * Spring Boot automatically adds every JacksonModule bean to the application JsonMapper.
 *
 * @param mappers every details mapper, whose DTO is registered under its account type code.
 */
@Component
class AccountDetailsJacksonModule(
    mappers: List<AccountDetailsMapperSolver<*, *>>,
) : SimpleModule("AccountDetailsJacksonModule") {
    init {
        registerSubtypes(*mappers.map { NamedType(it.dtoType, it.accountTypeCode.value) }.toTypedArray())
    }
}
