package com.example.finvest.modules.account.infrastructure.config

import com.example.finvest.modules.account.application.solver.DashboardAssuranceVieRepositorySolver
import com.example.finvest.modules.account.application.solver.DashboardCompteCourantRepositorySolver
import com.example.finvest.modules.account.application.solver.DashboardCompteTitreRepositorySolver
import com.example.finvest.modules.account.application.solver.DashboardLivretRepositorySolver
import com.example.finvest.modules.account.application.solver.DashboardPeaRepositorySolver
import com.example.finvest.modules.account.application.solver.DashboardPeeRepositorySolver
import com.example.finvest.modules.account.application.solver.DashboardPerRepositorySolver
import com.example.finvest.modules.account.application.solver.DashboardRepositorySolver
import com.example.finvest.modules.account.domain.cache.ReferenceDataCache
import com.example.finvest.modules.account.domain.models.AssuranceVie
import com.example.finvest.modules.account.domain.models.CompteCourant
import com.example.finvest.modules.account.domain.models.CompteTitre
import com.example.finvest.modules.account.domain.models.Livret
import com.example.finvest.modules.account.domain.models.Pea
import com.example.finvest.modules.account.domain.models.Pee
import com.example.finvest.modules.account.domain.models.Per
import com.example.finvest.modules.account.domain.repository.AssuranceVieRepository
import com.example.finvest.modules.account.domain.repository.CompteCourantRepository
import com.example.finvest.modules.account.domain.repository.CompteTitreRepository
import com.example.finvest.modules.account.domain.repository.LivretRepository
import com.example.finvest.modules.account.domain.repository.PeaRepository
import com.example.finvest.modules.account.domain.repository.PeeRepository
import com.example.finvest.modules.account.domain.repository.PerRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DashboardRepositorySolverConfig {
    @Bean
    fun dashboardAssuranceVieRepositorySolver(
        repository: AssuranceVieRepository,
        referenceDataCache: ReferenceDataCache,
    ): DashboardRepositorySolver<AssuranceVie> = DashboardAssuranceVieRepositorySolver(repository, referenceDataCache)

    @Bean
    fun dashboardCompteCourantRepositorySolver(
        repository: CompteCourantRepository,
        referenceDataCache: ReferenceDataCache,
    ): DashboardRepositorySolver<CompteCourant> = DashboardCompteCourantRepositorySolver(repository, referenceDataCache)

    @Bean
    fun dashboardCompteTitreRepositorySolver(
        repository: CompteTitreRepository,
        referenceDataCache: ReferenceDataCache,
    ): DashboardRepositorySolver<CompteTitre> = DashboardCompteTitreRepositorySolver(repository, referenceDataCache)

    @Bean
    fun dashboardLivretRepositorySolver(
        repository: LivretRepository,
        referenceDataCache: ReferenceDataCache,
    ): DashboardRepositorySolver<Livret> = DashboardLivretRepositorySolver(repository, referenceDataCache)

    @Bean
    fun dashboardPeaRepositorySolver(
        repository: PeaRepository,
        referenceDataCache: ReferenceDataCache,
    ): DashboardRepositorySolver<Pea> = DashboardPeaRepositorySolver(repository, referenceDataCache)

    @Bean
    fun dashboardPeeRepositorySolver(
        repository: PeeRepository,
        referenceDataCache: ReferenceDataCache,
    ): DashboardRepositorySolver<Pee> = DashboardPeeRepositorySolver(repository, referenceDataCache)

    @Bean
    fun dashboardPerRepositorySolver(
        repository: PerRepository,
        referenceDataCache: ReferenceDataCache,
    ): DashboardRepositorySolver<Per> = DashboardPerRepositorySolver(repository, referenceDataCache)
}
