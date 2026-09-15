package com.example.finvest.account.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("account_owners")
data class AccountOwnerEntity(
    @Id
    @Column("id")
    var id: Long = 0L,

    @Column("account_id")
    var accountId: Long = 0L,

    @Column("user_id")
    var userId: Long? = null,

    @Column("name")
    var name: String = "",

    @Column("ownership_percentage")
    var ownershipPercentage: BigDecimal = BigDecimal.ZERO
)