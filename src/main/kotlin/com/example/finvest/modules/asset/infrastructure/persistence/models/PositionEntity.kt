package com.example.finvest.modules.asset.infrastructure.persistence.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("positions")
data class PositionEntity(
    @Id
    @Column("id")
    var id: Long = 0L,
    @Column("account_id")
    var accountId: Long = 0L,
    @Column("asset_id")
    var assetId: Long = 0L,
    @Column("quantity")
    var quantity: BigDecimal = BigDecimal.ZERO,
    @Column("average_price")
    var averagePrice: BigDecimal = BigDecimal.ZERO,
    @Column("currency_id")
    var currencyId: Long = 0L,
)
