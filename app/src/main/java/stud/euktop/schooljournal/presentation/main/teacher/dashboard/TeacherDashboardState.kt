package stud.euktop.schooljournal.presentation.main.teacher.dashboard

import stud.euktop.schooljournal.presentation.common.base.BaseState

/**
 * Состояние экрана главной страницы учителя (Teacher Dashboard).
 */
data class TeacherDashboardState(
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<TeacherDashboardState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): TeacherDashboardState =
        copy(loadingMap = loadingMap)
}

