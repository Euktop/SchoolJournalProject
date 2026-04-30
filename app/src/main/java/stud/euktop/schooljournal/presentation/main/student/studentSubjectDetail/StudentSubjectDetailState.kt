package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class StudentSubjectDetailState(
    override val isLoading: Boolean = false,
    val marks: List<StudentSubjectMark> = emptyList()
) : BaseState<StudentSubjectDetailState>() {
    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}
