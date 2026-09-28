package com.example.finvest.modules.shared.application.manager

interface TransactionManager {
    fun <T> execute(block: () -> T): T
}
