package stud.euktop.data.repository

import stud.euktop.domain.model.dashboard.DashboardStatistics
import stud.euktop.domain.repository.DashboardRepository
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor() : DashboardRepository {
    override suspend fun getStatistics(): Result<DashboardStatistics> {
        // TODO: заменить на реальные вызовы API (SchoolsApi, UsersApi и т.д.)
        return Result.success(
            DashboardStatistics(
                schoolsCount = 12,
                activeUsersCount = 245,
                totalStudents = 3420,
                totalTeachers = 184,
                healthPercent = 98
            )
        )
    }
}