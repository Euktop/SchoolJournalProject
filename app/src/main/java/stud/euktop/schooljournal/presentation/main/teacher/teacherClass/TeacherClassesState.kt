package stud.euktop.schooljournal.presentation.main.teacher.teacherClass

import stud.euktop.domain.model.lesson.TeacherClassItem
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class TeacherClassesState(
    override val isLoading: Boolean = false,
    val classes: List<TeacherClassItem> = emptyList()
) : BaseState<TeacherClassesState>() {
    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}

