package stud.euktop.schooljournal.presentation.main.teacher.teacherLessons

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class TeacherLessonsState(
    val lessons: List<TeacherLessonItem> = emptyList(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<TeacherLessonsState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): TeacherLessonsState =
        copy(loadingMap = loadingMap)
}