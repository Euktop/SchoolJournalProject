package stud.euktop.schooljournal.presentation.main.admin.schools

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import javax.inject.Inject

@HiltViewModel
class SchoolsListViewModel @Inject constructor(
    private val schoolRepository: SchoolAdminRepository,
    private val routerAdmin: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<SchoolsListState, Unit>() {

    init {
        executeCoordinator = coordinatorExec
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

    override fun initState() = SchoolsListState()
    private var currentOffset = 0
    var hasMore = true

    fun loadNextPage() {
        if (isLoading("pagination") || !hasMore) return
        val offset = currentOffset
        executeWithLoadingSync(
            key = "pagination",
            block = {
                schoolRepository.getSchools(
                    _state.value.filter.copy(pagination = Pagination(offset, PAGE_SIZE))
                )
            }
        ) { newSchools ->
            _state.update { it.copy(schools = if (offset == 0) newSchools else it.schools + newSchools) }
            currentOffset += newSchools.size
            hasMore = newSchools.size == PAGE_SIZE
        }
    }

    fun applyFilter(filter: SchoolFilter) {
        _state.update { it.copy(filter = filter, schools = emptyList()) }
        currentOffset = 0
        hasMore = true
        loadNextPage()
    }

    fun editSchool(school: School) = routerAdmin.toEditSchool(school.schoolId)
    fun deleteSchool(schoolId: Int) {
        executeWithLoadingSync("delete", { schoolRepository.deleteSchool(schoolId) }) { refresh() }
    }

    private fun refresh() {
        currentOffset = 0
        hasMore = true
        _state.update { it.copy(schools = emptyList()) }
        loadNextPage()
    }
}