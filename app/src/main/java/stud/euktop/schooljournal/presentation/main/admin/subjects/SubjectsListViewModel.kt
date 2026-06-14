package stud.euktop.schooljournal.presentation.main.admin.subjects

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.repository.SubjectAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import javax.inject.Inject

@HiltViewModel
class SubjectsListViewModel @Inject constructor(
    private val subjectRepository: SubjectAdminRepository,
    private val routerAdmin: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<SubjectsListState, Unit>() {
    init {
        executeCoordinator = coordinatorExec
    }
    companion object {
        private const val PAGE_SIZE = 20
    }

    override fun initState() = SubjectsListState()
    private var currentOffset = 0
    var hasMore = true

    fun loadNextPage() {
        if (isLoading("pagination") || !hasMore) return
        val offset = currentOffset
        executeWithResultLoadingSync(
            key = "pagination",
            block = {
                subjectRepository.getSubjects(
                    _state.value.filter.copy(
                        pagination = Pagination(
                            offset,
                            PAGE_SIZE
                        )
                    )
                )
            }
        ) { newSubjects ->
            _state.update { it.copy(subjects = if (offset == 0) newSubjects else it.subjects + newSubjects) }
            currentOffset += newSubjects.size
            hasMore = newSubjects.size == PAGE_SIZE
        }
    }

    fun applyFilter(filter: SubjectFilter) {
        _state.update { it.copy(filter = filter, subjects = emptyList()) }
        currentOffset = 0
        hasMore = true
        loadNextPage()
    }

    fun editSubject(subject: Subject) = routerAdmin.toAdminEditSubject(subject.subjectId)
    fun createNew() = routerAdmin.toAdminEditSubject(-1)
    fun deleteSubject(subjectId: Int) {
        executeWithResultLoadingSync(
            "delete",
            { subjectRepository.deleteSubject(subjectId) }) { refresh() }
    }

    private fun refresh() {
        currentOffset = 0
        hasMore = true
        _state.update { it.copy(subjects = emptyList()) }
        loadNextPage()
    }
}