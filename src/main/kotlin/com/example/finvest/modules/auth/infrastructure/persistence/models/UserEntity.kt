package com.example.finvest.modules.auth.infrastructure.persistence.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class UserEntity(
    @Id
    @Column("id")
    var id: Long = 0L,

    @Column("email")
    var email: String = "",

    @Column("password")
    var password: String = "",
)