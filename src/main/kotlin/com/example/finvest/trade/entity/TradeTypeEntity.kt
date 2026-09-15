package com.example.finvest.trade.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("trade_types")
data class TradeTypeEntity(
    @Id
    @Column("id")
    var id: Long = 0L,

    @Column("code")
    var code: String = "",

    @Column("name")
    var name: String = ""
)