package stud.euktop.schooljournal.presentation.main.admin.classes

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.repository.ClassAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.filter.classes.AppClassInfoFilter
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import javax.inject.Inject

@HiltViewModel
class ClassesListViewModel @Inject constructor(
    private val classRepository: ClassAdminRepository,
    private val routerAdmin: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<ClassesListState, Unit>() {
    init {
        executeCoordinator = coordinatorExec
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

    override fun initState() = ClassesListState()
    private var currentOffset = 0
    var hasMore = true

    fun loadNextPage() {
        if (isLoading("pagination") || !hasMore) return
        val offset = currentOffset
        executeWithLoadingSync(
            key = "pagination",
            block = {
                classRepository.getClasses(
                    _state.value.filter.toDomain()
                        .copy(pagination = Pagination(offset, PAGE_SIZE))
                )
            }
        ) { newClasses ->
            val isFirstPage = offset == 0
            _state.update { it.copy(classes = if (isFirstPage) newClasses else it.classes + newClasses) }
            currentOffset += newClasses.size
            hasMore = newClasses.size == PAGE_SIZE
        }
    }

    fun applyFilter(filter: AppClassInfoFilter) {
        _state.update { it.copy(filter = filter, classes = emptyList()) }
        currentOffset = 0
        hasMore = true
        loadNextPage()
    }

    fun editClass(classInfo: ClassInfo) = routerAdmin.toAdminEditClass(classInfo.classId)
    fun deleteClass(classId: Int) {
        executeWithLoadingSync("delete", { classRepository.deleteClass(classId) }) { refresh() }
    }

    private fun refresh() {
        currentOffset = 0
        hasMore = true
        _state.update { it.copy(classes = emptyList()) }
        loadNextPage()
    }
}