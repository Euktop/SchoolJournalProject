package stud.euktop.schooljournal.presentation.main.teacher

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class TeacherClassesState(
    override val isLoading: Boolean = false
) : BaseState<TeacherClassesState>() {
    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}

