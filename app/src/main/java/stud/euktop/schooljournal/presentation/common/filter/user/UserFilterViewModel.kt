package stud.euktop.schooljournal.presentation.common.filter.user

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import stud.euktop.domain.contract.RoleRepository
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseFilterViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class UserFilterViewModel @Inject constructor(
    private val schoolAdminRepository: SchoolAdminRepository,
    private val roleRepository: RoleRepository,
    coordinatorExec: CoordinatorExec
) : BaseFilterViewModel(coordinatorExec) {

    private val _roles = MutableStateFlow<List<Role>>(emptyList())
    val roles: StateFlow<List<Role>> = _roles

    private val _schools = MutableStateFlow<List<School>>(emptyList())
    val schools: StateFlow<List<School>> = _schools

    private val _statuses = MutableStateFlow<List<AccountStatus>>(emptyList())
    val statuses: StateFlow<List<AccountStatus>> = _statuses

    fun loadRoles() {
        execSync({ runCatching { roleRepository.getAvailableRoles() } }, { _roles.value = it })
    }

    fun loadSchools(filter: SchoolFilter = SchoolFilter()) {
        execSync({ schoolAdminRepository.getSchools(filter) }, { _schools.value = it })
    }

    fun loadStatuses() {
        _statuses.value = AccountStatus.entries.toList()
    }
}