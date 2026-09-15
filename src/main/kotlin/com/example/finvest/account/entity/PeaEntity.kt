package com.example.finvest.account.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate

@Table("peas")
data class PeaEntity(
    @Id
    @Column("account_id")
    var accountId: Long = 0L,

    @Column("opening_date")
    var openingDate: LocalDate? = null,

    @Column("deposit_limit")
    var depositLimit: BigDecimal? = null
)