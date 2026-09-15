package com.example.finvest.asset.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("assets")
data class AssetEntity(
    @Id
    @Column("id")
    var id: Long = 0L,

    @Column("name")
    var name: String = "",

    @Column("asset_type_id")
    var assetTypeId: Long = 0L,

    @Column("isin")
    var isin: String? = null,

    @Column("ticker")
    var ticker: String? = null,

    @Column("issuer_country_id")
    var issuerCountryId: Long? = null,

    @Column("currency_id")
    var currencyId: Long = 0L,

    @Column("description")
    var description: String? = null
)