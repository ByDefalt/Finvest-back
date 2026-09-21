package com.example.finvest.logger

interface LoggerFactory {
    fun getLogger(type: Class<*>): Logger
}