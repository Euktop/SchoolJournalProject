package stud.euktop.domain.repository

import stud.euktop.domain.model.dashboard.DashboardStatistics

interface DashboardRepository {
    suspend fun getStatistics(): Result<DashboardStatistics>
}