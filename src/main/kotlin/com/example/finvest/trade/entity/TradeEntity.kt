package com.example.finvest.trade.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("trades")
data class TradeEntity(
    @Id
    @Column("id")
    var id: Long = 0L,

    @Column("account_id")
    var accountId: Long = 0L,

    @Column("asset_id")
    var assetId: Long = 0L,

    @Column("trade_type_id")
    var tradeTypeId: Long = 0L,

    @Column("quantity")
    var quantity: BigDecimal = BigDecimal.ZERO,

    @Column("unit_price")
    var unitPrice: BigDecimal = BigDecimal.ZERO,

    @Column("fees")
    var fees: BigDecimal = BigDecimal.ZERO,

    @Column("currency_id")
    var currencyId: Long = 0L,

    @Column("date")
    var date: LocalDateTime = LocalDateTime.now()
)