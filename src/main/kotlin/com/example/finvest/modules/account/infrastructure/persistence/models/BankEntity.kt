package com.example.finvest.modules.account.infrastructure.persistence.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("banks")
data class BankEntity(
    @Id
    @Column("id")
    var id: Long = 0L,

    @Column("name")
    var name: String = "",

    @Column("bic")
    var bic: String? = null,

    @Column("logo")
    var logo: String? = null
)