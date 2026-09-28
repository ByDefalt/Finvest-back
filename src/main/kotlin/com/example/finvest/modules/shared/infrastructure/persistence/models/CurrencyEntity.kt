package com.example.finvest.modules.shared.infrastructure.persistence.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("currencies")
data class CurrencyEntity(
    @Id
    @Column("id")
    var id: Long = 0L,
    @Column("code")
    var code: String = "",
)
