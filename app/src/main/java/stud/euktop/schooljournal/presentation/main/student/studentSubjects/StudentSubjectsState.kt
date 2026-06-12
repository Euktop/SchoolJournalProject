package stud.euktop.schooljournal.presentation.main.student.studentSubjects

import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class StudentSubjectsState(
    val subjects: List<StudentSubjectSummary> = emptyList(),
    val overallAverage: Double? = null,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<StudentSubjectsState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): StudentSubjectsState =
        copy(loadingMap = loadingMap)
}