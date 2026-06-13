package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockDashboardDataSource
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.dashboard.DashboardStatistics
import stud.euktop.domain.repository.DashboardRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardMockRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler
) : DashboardRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getStatistics(): Result<DashboardStatistics> =
        apiErrorHandler.safeApiCall {
            logger?.d(tag, "getStatistics", "Fetching dashboard statistics")
            MockDelayService.delay()
            val stats = MockDashboardDataSource.getStatistics()
            logger?.i(
                tag,
                "getStatistics_success",
                "Stats retrieved: ${stats.totalStudents} students, ${stats.totalTeachers} teachers"
            )
            stats
        }
}