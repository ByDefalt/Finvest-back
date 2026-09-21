package com.example.finvest.modules.account.domain.models

data class Bank(
    var id: Long,
    var name: String,
    var bic: String?,
    var logo: String?
)
