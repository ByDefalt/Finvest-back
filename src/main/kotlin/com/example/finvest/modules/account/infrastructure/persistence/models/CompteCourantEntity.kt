package com.example.finvest.modules.account.infrastructure.persistence.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("compte_courants")
data class CompteCourantEntity(
    @Id
    @Column("account_id")
    var accountId: Long = 0L,

    @Column("iban")
    var iban: String? = null,

    @Column("bic")
    var bic: String? = null,

    @Column("account_number")
    var accountNumber: String? = null,

    @Column("overdraft_limit")
    var overdraftLimit: BigDecimal? = null,

    @Column("holder_name")
    var holderName: String? = null
)