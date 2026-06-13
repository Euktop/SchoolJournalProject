package stud.euktop.data.mock.data

import stud.euktop.domain.model.dashboard.DashboardStatistics

internal object MockDashboardDataSource {
    fun getStatistics(): DashboardStatistics {
        return DashboardStatistics(
            schoolsCount = 12,
            activeUsersCount = 245,
            totalStudents = 3420,
            totalTeachers = 184,
            healthPercent = 98
        )
    }
}