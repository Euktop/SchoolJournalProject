package stud.euktop.schooljournal.presentation.main.admin.users

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.AccountStatus
import stud.euktop.domain.model.Role
import stud.euktop.domain.model.RoleSchools
import stud.euktop.domain.model.UserInfo
import stud.euktop.domain.repository.AdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager,
    private val adminRepository: AdminRepository
) : BaseViewModel<UserEditState, UserEditEvent>() {

    companion object {
        const val KEY_USER_ID = "userId"
    }

    private val userId: Int = savedStateHandle[KEY_USER_ID] ?: 0

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        loadInitialData()
        if (userId != 0) loadUser()
    }

    override fun initState() = UserEditState(userId = userId)

    private fun loadInitialData() {
        executeWithCoordinatorAndLoadingSync(
            block = { adminRepository.getSchools() },
            onSuccess = { schools ->
                _state.update { it.copy(availableSchools = schools) }
            }
        )
        _state.update { it.copy(availableRoles = Role.entries.toList()) }
    }

    private fun loadUser() {
        executeWithCoordinatorAndLoadingSync(
            block = { adminRepository.getUser(userId) },
            onSuccess = { user ->
                val firstRoleSchool = user.roles.firstOrNull()
                _state.update {
                    it.copy(
                        userId = user.userId,
                        lastName = it.lastName.copy(user.lastName),
                        firstName = it.firstName.copy(user.firstName),
                        surName = it.surName.copy(user.surName ?: ""),
                        email = it.email.copy(user.email),
                        phone = it.phone.copy(user.phone ?: ""),
                        accountStatus = user.accountStatus,
                        selectedRole = firstRoleSchool?.role,
                        selectedSchoolId = firstRoleSchool?.schoolId
                    )
                }
            }
        )
    }

    fun updateLastName(value: String) {
        _state.update { it.copy(lastName = it.lastName.copy(value)) }
    }

    fun updateFirstName(value: String) {
        _state.update { it.copy(firstName = it.firstName.copy(value)) }
    }

    fun updateSurName(value: String) {
        _state.update { it.copy(surName = it.surName.copy(value)) }
    }

    fun updateEmail(value: String) {
        _state.update { it.copy(email = it.email.copy(value)) }
    }

    fun updatePhone(value: String) {
        _state.update { it.copy(phone = it.phone.copy(value)) }
    }

    fun updatePassword(value: String) {
        _state.update { it.copy(password = it.password.copy(value)) }
    }

    fun updateAccountStatus(status: AccountStatus) {
        _state.update { it.copy(accountStatus = status) }
    }

    fun updateSelectedRole(role: Role?) {
        _state.update {
            it.copy(
                selectedRole = role,
                selectedSchoolId = if (role == Role.ADMIN || role == null) null else it.selectedSchoolId
            )
        }
    }

    fun updateSelectedSchool(schoolId: Int?) {
        _state.update { it.copy(selectedSchoolId = schoolId) }
    }

    fun save() {
        val state = _state.value
        if (!state.isFormValid()) return

        val roles = state.selectedRole?.let { role ->
            listOf(RoleSchools(role, state.selectedSchoolId))
        } ?: emptyList()

        val user = UserInfo(
            userId = state.userId,
            lastName = state.lastName.getValidate(),
            firstName = state.firstName.getValidate(),
            surName = state.surName.value?.takeIf { it.isNotBlank() },
            email = state.email.getValidate(),
            phone = state.phone.value?.takeIf { it.isNotBlank() },
            accountStatus = state.accountStatus,
            roles = roles
        )

        executeWithCoordinatorAndLoadingSync(
            block = {
                if (state.isEditMode()) {
                    adminRepository.updateUser(user)
                } else {
                    adminRepository.addUser(user, state.password.value)
                }
            },
            onSuccess = { _event.emit(UserEditEvent.NavigateBack) }
        )
    }
}