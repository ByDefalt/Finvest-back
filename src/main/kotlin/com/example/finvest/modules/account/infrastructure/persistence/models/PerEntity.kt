package com.example.finvest.modules.account.infrastructure.persistence.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("pers")
data class PerEntity(
    @Id
    @Column("account_id")
    var accountId: Long = 0L,
    @Column("contract_number")
    var contractNumber: String = "",
    @Column("opening_date")
    var openingDate: LocalDate = LocalDate.now(),
    @Column("management_type_id")
    var managementTypeId: Long = 0L,
)
