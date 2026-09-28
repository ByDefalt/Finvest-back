package com.example.finvest.modules.asset.infrastructure.persistence.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("asset_prices")
data class AssetPriceEntity(
    @Id
    @Column("id")
    var id: Long = 0L,
    @Column("asset_id")
    var assetId: Long = 0L,
    @Column("price")
    var price: BigDecimal = BigDecimal.ZERO,
    @Column("currency_id")
    var currencyId: Long = 0L,
    @Column("date")
    var date: LocalDateTime = LocalDateTime.now(),
)
