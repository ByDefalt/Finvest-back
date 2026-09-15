package com.example.finvest.account.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("management_types")
data class ManagementTypeEntity(
    @Id
    @Column("id")
    var id: Long = 0L,

    @Column("code")
    var code: String = "",

    @Column("name")
    var name: String = ""
)