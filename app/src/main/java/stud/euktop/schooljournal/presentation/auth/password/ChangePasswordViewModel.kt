package stud.euktop.schooljournal.presentation.auth.password

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAuth
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val routerAuth: RouterAuth
) : BaseViewModel<ChangePasswordState, Unit>() {

    override fun initState() = ChangePasswordState()

    fun updateOldPassword(value: String) {
        _state.update { it.copy(oldPassword = it.oldPassword.copy(value)) }
    }

    fun updateNewPassword(value: String) {
        _state.update { it.copy(newPassword = it.newPassword.copy(value)) }
    }

    fun updateConfirmPassword(value: String) {
        _state.update { it.copy(confirmPassword = value) }
    }

    fun changePassword() {
        val state = _state.value
        if (!state.isFormValid()) return
        executeWithLoadingSync(
            key = "change_password",
            block = {
                authRepository.changePassword(
                    state.oldPassword.getValidate(),
                    state.newPassword.getValidate()
                )
            },
            onSuccess = { routerAuth.toSuccessChangePassword() }
        )
    }

    fun cancel() {
        viewModelScope.launch {
            routerAuth.toCancelChangePassword()
        }
    }
}