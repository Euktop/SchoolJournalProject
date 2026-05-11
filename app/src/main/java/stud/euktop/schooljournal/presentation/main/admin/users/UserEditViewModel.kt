package stud.euktop.schooljournal.presentation.main.admin.users

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.common.Field
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.model.user.UserUpdate
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserAdminRepository,
    private val routerAdmin: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<UserEditState, Unit>() {

    private val userId: Int = savedStateHandle["userId"] ?: 0
    private val isEditMode get() = userId != 0

    override fun initState() = UserEditState(userId = userId)

    init {
        executeCoordinator = coordinatorExec
        if (isEditMode) loadUser()
    }

    private fun loadUser() {
        executeWithLoadingSync(
            key = "load",
            block = { userRepository.getUser(userId) },
            onSuccess = { user ->
                _state.update {
                    it.copy(
                        lastName = it.lastName.copy(user.lastName),
                        firstName = it.firstName.copy(user.firstName),
                        surName = it.surName.copy(user.surName ?: ""),
                        email = it.email.copy(user.email),
                        phone = it.phone.copy(user.phone ?: ""),
                        accountStatus = user.accountStatus,
                        selectedRoles = user.roles.map { RoleWithSchool(it.role, it.schoolId) }
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

    fun addRole(role: Role, schoolId: Int?) {
        val newRole = RoleWithSchool(role, schoolId)
        _state.update { it.copy(selectedRoles = it.selectedRoles + newRole) }
    }

    fun removeRole(role: RoleWithSchool) {
        _state.update { it.copy(selectedRoles = it.selectedRoles.filter { it != role }) }
    }

    fun save() {
        val state = _state.value
        if (!state.isFormValid()) return
        if (isEditMode) {
            val update = UserUpdate(
                userId = userId,
                lastName = Field(state.lastName.getValidate(), state.lastName.value != null),
                firstName = Field(state.firstName.getValidate(), true),
                surName = Field(state.surName.getValidate().takeIf { it.isNotBlank() }, true),
                email = Field(state.email.getValidate(), true),
                phone = Field(state.phone.value, true),
                accountStatus = Field(state.accountStatus, true)
            )
            executeWithLoadingSync(
                "save",
                { userRepository.updateUser(update) }) { routerAdmin.navigateBack() }
        } else {
            val profile = UserProfile.createObject(
                lastName = state.lastName.getValidate(),
                firstName = state.firstName.getValidate(),
                surName = state.surName.getValidate().takeIf { it.isNotBlank() },
                email = state.email.getValidate(),
                phone = state.phone.value,
                accountStatus = state.accountStatus
            )
            executeWithLoadingSync(
                "save",
                {
                    userRepository.addUser(
                        profile,
                        state.password.value
                    )
                }) { routerAdmin.navigateBack() }
        }
    }

    fun cancel() {
        routerAdmin.navigateBack()
    }
}