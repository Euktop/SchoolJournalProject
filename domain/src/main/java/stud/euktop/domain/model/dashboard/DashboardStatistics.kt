package stud.euktop.domain.model.dashboard

data class DashboardStatistics(
    val schoolsCount: Int,
    val activeUsersCount: Int,
    val totalStudents: Int,
    val totalTeachers: Int,
    val healthPercent: Int
)