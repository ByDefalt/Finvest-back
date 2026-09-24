package com.example.finvest.modules.shared.infrastructure.persistence

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import

// TestDatabaseConfiguration.kt
@TestConfiguration(proxyBeanMethods = false)
@Import(MariaDbTestConfiguration::class)
class TestDatabaseConfiguration