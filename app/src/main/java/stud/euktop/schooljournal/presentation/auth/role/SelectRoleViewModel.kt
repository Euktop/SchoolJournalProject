package stud.euktop.schooljournal.presentation.auth.role

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.model.user.Role
import stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator
import stud.euktop.schooljournal.presentation.auth.role.RoleItem.Companion.toItem
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAuth
import javax.inject.Inject

@HiltViewModel
class SelectRoleViewModel @Inject constructor(
    private val routerAuth: RouterAuth,
    private val authCoordinator: AuthCoordinator
) : BaseViewModel<SelectRoleState, Unit>() {
    init {
        updateRoles()
    }

    fun updateRoles() {
        executeCoordinatorResultLoadingBlockSync("role", {
            authCoordinator.getRoles()
        }) { roles ->
            _state.update { it -> it.copy(roles = roles.map { it.toItem() }) }
        }
    }

    override fun initState() = SelectRoleState()

    fun selectRole(role: Role) {
        _state.update { state ->
            state.copy(
                selectedRole = role,
                roles = state.roles.map { it.copy(isSelected = it.role == role) })
        }
    }

    fun onBack() {
        viewModelScope.launch {
            routerAuth.toBack()
        }
    }

    fun onContinueClick() {
        val selectedRole = _state.value.selectedRole ?: return
        executeCoordinatorResultLoadingBlockSync("save_role", { authCoordinator.saveRole(selectedRole) }, {
            viewModelScope.launch {
                routerAuth.toAfterSelectRole()
            }
        })
    }
}