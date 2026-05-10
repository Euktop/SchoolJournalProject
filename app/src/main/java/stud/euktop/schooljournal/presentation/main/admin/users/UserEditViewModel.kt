package stud.euktop.schooljournal.presentation.main.admin.users

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.contract.RoleRepository
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.UserRole
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager,
    private val userAdminRepository: UserAdminRepository,
    private val roleRepository: RoleRepository
) : BaseViewModel<UserEditState, UserEditEvent>() {

    companion object {
        const val KEY_USER_ID = "userId"
    }

    private val userId: Int = savedStateHandle[KEY_USER_ID] ?: 0
    private var canEditFull = false

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        initData()
    }

    override fun initState() = UserEditState(userId = userId)
    fun initData() {
        if (userId != 0) loadUser()
        checkEditPermissions()
    }

    private fun checkEditPermissions() {
        executeWithCoordinatorAndLoadingSync(
            block = { runCatching { roleRepository.canEditUser(userId) } },
            onSuccess = { canEdit ->
                canEditFull = canEdit
            }
        )
    }

    fun removeRole(role: UserRole) {
        _state.update { it ->
            it.copy(selectedRoles = it.selectedRoles.filter { it != role })
        }
    }

    fun addRole(role: UserRole) {
        _state.update { it ->
            it.copy(
                selectedRoles = it.selectedRoles.toMutableList()
                    .apply { if (!this.contains(role)) add(role) })
        }
    }

    private fun loadUser() {
        executeWithCoordinatorAndLoadingSync(
            block = { userAdminRepository.getUser(userId) },
            onSuccess = { user ->
                _state.update {
                    it.copy(
                        userId = user.userId,
                        lastName = it.lastName.copy(user.lastName),
                        firstName = it.firstName.copy(user.firstName),
                        surName = it.surName.copy(user.surName ?: ""),
                        email = it.email.copy(user.email),
                        phone = it.phone.copy(user.phone ?: ""),
                        accountStatus = user.accountStatus,
                        selectedRoles = user.roles
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

    fun canEditFull(): Boolean = canEditFull

    fun save() {
        val state = _state.value
        if (!state.isFormValid()) return

        val roles = state.selectedRoles.map { (role, school) ->
            UserRole(role, school)
        }

        val user = UserProfile(
            userId = state.userId,
            lastName = state.lastName.getValidate(),
            firstName = state.firstName.getValidate(),
            surName = state.surName.value?.takeIf { it.isNotBlank() },
            birthday = null,
            gender = stud.euktop.domain.model.user.Gender.NONE,
            email = state.email.getValidate(),
            phone = state.phone.value?.takeIf { it.isNotBlank() },
            roles = roles,
            dateRegistration = Date(),
            accountStatus = if (canEditFull) state.accountStatus else _state.value.accountStatus
        )

        executeWithCoordinatorAndLoadingSync(
            block = {
                if (state.isEditMode()) userAdminRepository.updateUser(user)
                else userAdminRepository.addUser(user, state.password.value)
            },
            onSuccess = { _event.emit(UserEditEvent.NavigateBack) }
        )
    }
}