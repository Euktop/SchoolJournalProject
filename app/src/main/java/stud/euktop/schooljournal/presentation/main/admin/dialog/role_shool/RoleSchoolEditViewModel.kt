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
) : BaseViewModel<RoleSchoolEditState, Unit>() {

    private val _schools = MutableStateFlow<List<School>>(emptyList())
    val schools: StateFlow<List<School>> = _schools

    private val _roles = MutableStateFlow<List<Role>>(emptyList())
    val roles: StateFlow<List<Role>> = _roles

    init {
        executeCoordinator = coordinatorExec
    }

    fun loadSchools(filter: SchoolFilter) {
        executeWithLoadingSync(
            key = "load_schools",
            block = { schoolAdminRepository.getSchools(filter) },
            onSuccess = { schools -> _schools.update { schools } }
        )
    }

    fun loadRoles() {
        executeWithLoadingSync(
            key = "load_roles",
            block = { runCatching { roleRepository.getAvailableRoles() } },
            onSuccess = { roles -> _roles.update { roles } }
        )
    }

    override fun initState() = RoleSchoolEditState()
}