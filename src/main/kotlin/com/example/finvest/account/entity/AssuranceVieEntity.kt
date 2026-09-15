package com.example.finvest.account.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("assurances_vie")
data class AssuranceVieEntity(
    @Id
    @Column("account_id")
    var accountId: Long = 0L,

    @Column("contract_number")
    var contractNumber: String? = null,

    @Column("opening_date")
    var openingDate: LocalDate? = null,

    @Column("management_type_id")
    var managementTypeId: Long? = null
)