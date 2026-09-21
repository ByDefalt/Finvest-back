package com.example.finvest.modules.asset.entity

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("asset_countries")
data class AssetCountryEntity(
    @Column("asset_id")
    var assetId: Long = 0L,

    @Column("country_id")
    var countryId: Long = 0L,

    @Column("weight")
    var weight: BigDecimal = BigDecimal.ZERO
)