package com.example.finvest.modules.transaction.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("transaction_types")
data class TransactionTypeEntity(
    @Id
    @Column("id")
    var id: Long = 0L,

    @Column("code")
    var code: String = "",
)