package com.example.finvest.transaction.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("transactions")
data class TransactionEntity(
    @Id
    @Column("id")
    var id: Long = 0L,

    @Column("account_id")
    var accountId: Long = 0L,

    @Column("transaction_type_id")
    var transactionTypeId: Long = 0L,

    @Column("amount")
    var amount: BigDecimal = BigDecimal.ZERO,

    @Column("currency_id")
    var currencyId: Long = 0L,

    @Column("date")
    var date: LocalDateTime = LocalDateTime.now(),

    @Column("description")
    var description: String? = null
)