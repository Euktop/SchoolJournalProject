package stud.euktop.schooljournal.presentation.main.admin.assignments

import stud.euktop.domain.model.assignment.TeacherAssignment
import stud.euktop.domain.model.assignment.TeacherAssignmentFilter
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class AssignmentsListState(
    val assignments: List<TeacherAssignment> = emptyList(),
    val filter: TeacherAssignmentFilter = TeacherAssignmentFilter(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<AssignmentsListState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}