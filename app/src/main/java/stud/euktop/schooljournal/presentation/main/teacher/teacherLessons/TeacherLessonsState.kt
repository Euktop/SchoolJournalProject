package stud.euktop.schooljournal.presentation.main.teacher.teacherLessons

import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.filter.lesson.AppLessonFilter

data class TeacherLessonsState(
    val lessons: List<TeacherLessonItem> = emptyList(),
    val filter: AppLessonFilter = AppLessonFilter(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<TeacherLessonsState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}