package com.example.finvest.modules.account.infrastructure.persistence.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("livrets")
data class LivretEntity(
    @Id
    @Column("account_id")
    var accountId: Long = 0L,

    @Column("interest_rate")
    var interestRate: BigDecimal? = null,

    @Column("ceiling")
    var ceiling: BigDecimal? = null
)