package stud.euktop.schooljournal.presentation.main.teacher.dashboard

import stud.euktop.schooljournal.presentation.common.base.BaseState

/**
 * Состояние экрана главной страницы учителя (Teacher Dashboard).
 *
 * Содержит данные для отображения приветствия, статистики, и информацию о следующем уроке.
 */
data class TeacherDashboardState(
    val teacherName: String = "",
    val nextLessonInfo: String = "",
    val lessonsCount: Int = 0,
    val classesCount: Int = 0,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<TeacherDashboardState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): TeacherDashboardState =
        copy(loadingMap = loadingMap)
}


