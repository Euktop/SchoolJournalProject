package stud.euktop.schooljournal.presentation.main.student.dashboard

import stud.euktop.schooljournal.presentation.common.base.BaseState

/**
 * Состояние экрана главной страницы ученика (Student Dashboard).
 *
 * Содержит данные для отображения приветствия, статистики (ДЗ, оценки, предметы),
 * а также информацию о следующем уроке.
 *
 * @property studentName Имя ученика для приветствия.
 * @property newHomeworksCount Количество новых домашних заданий.
 * @property averageMark Средний балл ученика.
 * @property lessonsTodayCount Количество уроков сегодня.
 * @property subjectsCount Количество предметов.
 * @property homeworksNotSubmitted Количество не сданных домашних заданий.
 * @property nextLessonName Название следующего урока.
 * @property nextLessonDetails Детали следующего урока (кабинет, учитель).
 * @property nextLessonTime Время до следующего урока.
 * @property isNextLessonVisible Флаг, показывающий, есть ли следующий урок.
 */
data class StudentDashboardState(
    val studentName: String? = null,
    val newHomeworksCount: Int = 0,
    val averageMark: Double? = null,
    val lessonsTodayCount: Int = 0,
    val subjectsCount: Int = 0,
    val homeworksNotSubmitted: Int = 0,
    val nextLessonName: String = "",
    val nextLessonDetails: String = "",
    val nextLessonTime: Pair<Int, List<Any?>>? = null,
    val isNextLessonVisible: Boolean = false,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<StudentDashboardState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): StudentDashboardState =
        copy(loadingMap = loadingMap)
}