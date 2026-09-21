package com.example.finvest.modules.account.infrastructure.persistence.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("pees")
data class PeeEntity(
    @Id
    @Column("account_id")
    var accountId: Long = 0L,

    @Column("opening_date")
    var openingDate: LocalDate? = null,

    @Column("employer")
    var employer: String? = null
)