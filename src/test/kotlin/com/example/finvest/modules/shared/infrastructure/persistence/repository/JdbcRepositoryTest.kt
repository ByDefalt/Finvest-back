package com.example.finvest.modules.shared.infrastructure.persistence.repository

import com.example.finvest.modules.shared.infrastructure.persistence.RealDatabaseTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@DataJdbcTest
@RealDatabaseTest
abstract class JdbcRepositoryTest {
    @Autowired
    protected lateinit var jdbcTemplate: NamedParameterJdbcTemplate
}