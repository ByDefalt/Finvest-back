package com.example.finvest.account.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("accounts")
data class AccountEntity(
    @Id
    @Column("id")
    var id: Long = 0L,

    @Column("bank_id")
    var bankId: Long = 0L,

    @Column("name")
    var name: String = "",

    @Column("balance")
    var balance: BigDecimal = BigDecimal.ZERO,

    @Column("currency_id")
    var currencyId: Long = 0L,

    @Column("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column("closed_at")
    var closedAt: LocalDateTime? = null,

    @Column("account_status_id")
    var accountStatusId: Long = 0L,

    @Column("description")
    var description: String? = null,

    @Column("account_type_id")
    var accountTypeId: Long = 0L,
)