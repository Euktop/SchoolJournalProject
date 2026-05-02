package stud.euktop.schooljournal.presentation.main.admin.dialog.role_shool

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import stud.euktop.domain.contract.RoleRepository
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

@HiltViewModel
class RoleSchoolEditViewModel @Inject constructor(
    private val schoolAdminRepository: SchoolAdminRepository,
    private val roleRepository: RoleRepository,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager
) : BaseViewModel<RoleSchoolEditState, Unit>() {

    private val _schools = MutableStateFlow<List<School>>(emptyList())
    val schools: StateFlow<List<School>> = _schools

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
    }

    fun loadSchools(filter: SchoolFilter) {
        executeWithCoordinatorAndLoadingSync(
            block = { schoolAdminRepository.getSchools(filter) },
            onSuccess = { schools -> _schools.update { schools } }
        )
    }

    private val _role = MutableStateFlow<List<Role>>(emptyList())
    val role: StateFlow<List<Role>> = _role

    fun loadRole() {
        executeWithCoordinatorAndLoadingSync(
            { runCatching { roleRepository.getAvailableRoles() } },
            { role -> _role.update { role } }
        )
    }

    override fun initState() = RoleSchoolEditState()
}