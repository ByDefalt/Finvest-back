package com.example.finvest.account.domain

data class Bank(
    var id: Long,
    var name: String,
    var bic: String?,
    var logo: String?
)
