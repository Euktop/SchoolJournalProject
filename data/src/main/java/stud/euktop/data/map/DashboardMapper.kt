package stud.euktop.data.map

import com.schooljournal.model.GetDashboardStatisticsResult
import stud.euktop.domain.model.dashboard.DashboardStatistics

internal fun GetDashboardStatisticsResult.toDomain(): DashboardStatistics {
    return DashboardStatistics(
        schoolsCount = this.totalSchools ?: 0,
        activeUsersCount = this.activeSessions ?: 0,
        totalStudents = this.totalStudents ?: 0,
        totalTeachers = this.totalTeachers ?: 0,
        healthPercent = this.systemHealthPercent ?: 0
    )
}