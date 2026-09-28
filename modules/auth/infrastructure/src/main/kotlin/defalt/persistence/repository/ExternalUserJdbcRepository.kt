package defalt.persistence.repository

import defalt.logger.Logger
import defalt.repository.ExternalUserRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ExternalUserJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val logger: Logger
) : ExternalUserRepository {
}