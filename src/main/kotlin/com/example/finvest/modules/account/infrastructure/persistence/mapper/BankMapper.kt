package com.example.finvest.modules.account.infrastructure.persistence.mapper

import com.example.finvest.modules.account.domain.models.Bank
import com.example.finvest.modules.account.domain.valueobject.BankId
import com.example.finvest.modules.account.domain.valueobject.Bic
import com.example.finvest.modules.account.infrastructure.persistence.models.BankEntity

fun BankEntity.toDomain(): Bank =
    Bank(
        id = BankId(this.id),
        name = this.name,
        bic = Bic(this.bic),
        logo = this.logo,
    )
