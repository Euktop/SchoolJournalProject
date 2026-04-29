package stud.euktop.schooljournal.presentation.main.student.studentSubjects

import stud.euktop.domain.model.StudentSubjectSummary
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class StudentSubjectsState(
    override val isLoading: Boolean = false,
    val subjects: List<StudentSubjectSummary> = emptyList()
) : BaseState<StudentSubjectsState>() {
    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}