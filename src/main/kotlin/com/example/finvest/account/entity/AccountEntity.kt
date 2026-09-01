package com.example.finvest.account.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("accounts")
data class AccountEntity(
    @Id
    val id: Long? = null,
    val name: String,
    val balance: BigDecimal,
    val type: String,
    @Column("user_id")
    val userId: Long
)