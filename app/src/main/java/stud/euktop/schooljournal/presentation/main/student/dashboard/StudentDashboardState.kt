package stud.euktop.schooljournal.presentation.main.student.dashboard

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class StudentDashboardState(
    val studentName: String = "Ученик",
    val newHomeworksCount: Int = 3,
    val averageMark: Double = 4.8,
    val lessonsTodayCount: Int = 5,
    val subjectsCount: Int = 8,
    val homeworksNotSubmitted: Int = 3,
    val nextLessonName: String = "Высшая математика",
    val nextLessonDetails: String = "Каб. 402 • Проф. Ричардсон",
    val nextLessonTime: String = "ЧЕРЕЗ 15 МИН",
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<StudentDashboardState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): StudentDashboardState =
        copy(loadingMap = loadingMap)
}