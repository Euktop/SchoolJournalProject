package stud.euktop.schooljournal.presentation.main.teacher.lessonMarks

import stud.euktop.domain.model.StudentMarkItem
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class LessonMarksState(
    override val isLoading: Boolean = false,
    val marks: List<StudentMarkItem> = emptyList()
) : BaseState<LessonMarksState>() {
    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}

