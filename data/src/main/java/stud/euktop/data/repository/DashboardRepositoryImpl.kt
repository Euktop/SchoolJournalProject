package stud.euktop.data.repository

import com.schooljournal.api.DashboardApi
import stud.euktop.data.map.toDomain
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.dashboard.DashboardStatistics
import stud.euktop.domain.repository.DashboardRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepositoryImpl @Inject constructor(
    private val dashboardApi: DashboardApi,
    private val errorHandler: ApiErrorHandler
) : DashboardRepository {

    override suspend fun getStatistics(): Result<DashboardStatistics> = errorHandler.safeApiCall {
        dashboardApi.apiDashboardStatisticsGet(schoolId = null).toDomain()
    }
}