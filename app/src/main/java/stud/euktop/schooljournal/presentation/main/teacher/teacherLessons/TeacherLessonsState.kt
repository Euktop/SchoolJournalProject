package stud.euktop.schooljournal.presentation.main.teacher.teacherLessons

import stud.euktop.domain.model.lesson.TeacherLessonItem
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class TeacherLessonsState(
    override val isLoading: Boolean = false,
    val lessons: List<TeacherLessonItem> = emptyList()
) : BaseState<TeacherLessonsState>() {
    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}

