package stud.euktop.data.mock.repository

import stud.euktop.domain.model.dashboard.DashboardStatistics
import stud.euktop.domain.repository.DashboardRepository
import javax.inject.Inject

class DashboardMockRepositoryImpl @Inject constructor() : DashboardRepository {
    override suspend fun getStatistics(): Result<DashboardStatistics> {
        // Здесь можно вернуть другие mock-данные
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