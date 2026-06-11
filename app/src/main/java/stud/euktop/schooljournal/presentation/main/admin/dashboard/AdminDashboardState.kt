package stud.euktop.schooljournal.presentation.main.admin.dashboard

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class AdminDashboardState(
    val adminName: String = "",
    val schoolsCount: Int = 0,
    val activeUsersCount: Int = 0,
    val totalStudents: Int = 0,
    val totalTeachers: Int = 0,
    val healthPercent: Int = 0,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<AdminDashboardState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}