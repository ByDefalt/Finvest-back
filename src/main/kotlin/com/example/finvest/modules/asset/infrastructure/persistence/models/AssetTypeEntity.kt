package com.example.finvest.modules.asset.infrastructure.persistence.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("asset_types")
data class AssetTypeEntity(
    @Id
    @Column("id")
    var id: Long = 0L,
    @Column("code")
    var code: String = "",
)
