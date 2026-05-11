package stud.euktop.schooljournal.presentation.main.admin.assignments

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.model.assignment.TeacherAssignment
import stud.euktop.domain.model.assignment.TeacherAssignmentFilter
import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.repository.AssignmentAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import javax.inject.Inject

@HiltViewModel
class AssignmentsListViewModel @Inject constructor(
    private val assignmentRepository: AssignmentAdminRepository,
    private val routerAdmin: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<AssignmentsListState, Unit>() {
    init {
        executeCoordinator = coordinatorExec
    }
    companion object {
        private const val PAGE_SIZE = 20
    }

    override fun initState() = AssignmentsListState()
    private var currentOffset = 0
    var hasMore = true

    fun loadNextPage() {
        if (isLoading("pagination") || !hasMore) return
        val offset = currentOffset
        val filterWithPagination =
            _state.value.filter.copy(pagination = Pagination(offset, PAGE_SIZE))
        executeWithLoadingSync(
            key = "pagination",
            block = { assignmentRepository.getTeacherAssignments(filterWithPagination) }
        ) { newAssignments ->
            _state.update { it.copy(assignments = if (offset == 0) newAssignments else it.assignments + newAssignments) }
            currentOffset += newAssignments.size
            hasMore = newAssignments.size == PAGE_SIZE
        }
    }

    fun applyFilter(filter: TeacherAssignmentFilter) {
        _state.update { it.copy(filter = filter, assignments = emptyList()) }
        currentOffset = 0
        hasMore = true
        loadNextPage()
    }

    fun editAssignment(assignment: TeacherAssignment) =
        routerAdmin.toEditAssignment(assignment.assignmentId)

    fun deleteAssignment(assignmentId: AssignmentId) {
        executeWithLoadingSync(
            "delete",
            { assignmentRepository.deleteTeacherAssignment(assignmentId) }) { refresh() }
    }

    private fun refresh() {
        currentOffset = 0
        hasMore = true
        _state.update { it.copy(assignments = emptyList()) }
        loadNextPage()
    }
}