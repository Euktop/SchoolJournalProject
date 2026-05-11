package stud.euktop.schooljournal.presentation.main.admin.users

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(
    private val userAdminRepository: UserAdminRepository,
    private val schoolRepository: SchoolAdminRepository,
    private val routerAdmin: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<UsersListState, Unit>() {
    init {
        executeCoordinator = coordinatorExec
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

    override fun initState() = UsersListState()

    private var currentOffset = 0
    var hasMore = true

    fun loadNextPage() {
        if (isLoading("pagination") || !hasMore) return
        val offset = currentOffset
        executeWithLoadingSync(
            key = "pagination",
            block = {
                userAdminRepository.getUsers(
                    _state.value.filter.copy(pagination = Pagination(offset, PAGE_SIZE))
                )
            },
            onSuccess = { newUsers ->
                val isFirstPage = offset == 0
                _state.update {
                    it.copy(
                        users = if (isFirstPage) newUsers else it.users + newUsers
                    )
                }
                currentOffset += newUsers.size
                hasMore = newUsers.size == PAGE_SIZE
            }
        )
    }

    fun applyFilter(filter: UserFilter) {
        _state.update { it.copy(filter = filter, users = emptyList()) }
        currentOffset = 0
        hasMore = true
        loadNextPage()
    }

    suspend fun CoroutineScope.getSchoolUserFilter(): School? {
        return executeCoordinatorResult {
            _state.value.filter.schoolId?.let { schoolRepository.getSchool(it) }
                ?: Result.success(null)
        }.await()
    }

    fun deleteUser(userId: Int) {
        executeWithLoadingSync(
            key = "delete",
            block = { userAdminRepository.deleteUser(userId) },
            onSuccess = { refresh() }
        )
    }

    fun editUser(user: UserListItem) {
        routerAdmin.toEditUser(user.userId)
    }

    private fun refresh() {
        currentOffset = 0
        hasMore = true
        _state.update { it.copy(users = emptyList()) }
        loadNextPage()
    }
}