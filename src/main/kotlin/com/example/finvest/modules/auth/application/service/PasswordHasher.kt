package com.example.finvest.modules.auth.application.service

interface PasswordHasher {

    fun hash(password: String): String

    fun matches(
        password: String,
        hash: String
    ): Boolean
}