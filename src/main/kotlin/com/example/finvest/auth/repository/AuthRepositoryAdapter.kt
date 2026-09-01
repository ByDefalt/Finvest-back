package com.example.finvest.auth.repository

import com.example.finvest.auth.domain.User
import com.example.finvest.auth.domain.UserCredentials
import com.example.finvest.common.logger.Logger
import com.example.finvest.auth.mapper.toUserCredentialsDomain
import com.example.finvest.auth.mapper.toUserDomain
import org.springframework.stereotype.Repository

@Repository
class AuthRepositoryAdapter(
    private val authJdbcRepository: AuthJdbcRepository,
    private val logger: Logger,
) : AuthRepository {

    override fun register(email: String, password: String): User {
        logger.debug("AuthRepositoryAdapter.register - inserting email=${email}")
        return authJdbcRepository.register(email, password).toUserDomain()
    }

    override fun findCredentialsByEmail(email: String): UserCredentials? {
        logger.debug("AuthRepositoryAdapter.findByEmail - querying email=$email")
        val user = authJdbcRepository
            .findByEmail(email)
            ?.toUserCredentialsDomain()
        logger.debug("AuthRepositoryAdapter.findByEmail - result found=${user != null} email=$email")
        return user
    }

    override fun findUserByEmail(email: String): User? {
        logger.debug("AuthRepositoryAdapter.findUserByEmail - querying email=$email")
        val user = authJdbcRepository
            .findByEmail(email)
            ?.toUserDomain()
        logger.debug("AuthRepositoryAdapter.findUserByEmail - result found=${user != null} email=$email")
        return user
    }
}