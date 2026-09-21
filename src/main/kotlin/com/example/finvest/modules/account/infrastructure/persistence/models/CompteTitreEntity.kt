package com.example.finvest.modules.account.infrastructure.persistence.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("comptes_titres")
data class CompteTitreEntity(
    @Id
    @Column("account_id")
    var accountId: Long = 0L,

    @Column("account_number")
    var accountNumber: String? = null
)