package com.example.finvest.auth.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class UserEntity(
    @Id
    val id: Long? = null,
    val email: String,
    val password: String,
)