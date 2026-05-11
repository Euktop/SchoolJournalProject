package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class StudentSubjectDetailState(
    val marks: List<StudentSubjectMark> = emptyList(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<StudentSubjectDetailState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): StudentSubjectDetailState = copy(loadingMap = loadingMap)
}