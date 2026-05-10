package stud.euktop.schooljournal.presentation.auth.password

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.binding.CreatePasswordFormActions
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAuthorization
import javax.inject.Inject

/**
 * ViewModel для экрана создания пароля.
 *
 * Назначение: управляет вводом и валидацией паролей.
 *
 * Функционал:
 * - State: passwordValidator, nextPasswordNextValidator, isLoading
 * - Методы обновления полей пароля
 * - isNextActive() – проверяет валидность и совпадение паролей
 * - onSaveClick() – вызывает AuthCoordinator.register с паролем
 * - При успехе отправляет событие Unit для перехода в главное меню
 *
 * @see CreatePasswordFragment
 */
@HiltViewModel
class CreatePasswordViewModel @Inject constructor(
    private val authCoordinator: AuthCoordinator,
    private val routerAuthorization: RouterAuthorization
) : BaseViewModel<CreatePasswordState, Unit>() {
    override fun initState() = CreatePasswordState()
    val updateState = object : CreatePasswordFormActions {
        override fun updatePassword(value: String) {
            _state.update { it.copy(password = it.password.copy(value)) }
        }

        override fun updateConfirmPassword(value: String) {
            _state.update { it.copy(confirmPassword = value) }
        }
    }

    fun onSaveClick() {
        if (!_state.value.isNextActive()) return
        executeLoadingBlockSync(
            key = "register",
            block = { authCoordinator.register(_state.value.password.getValidate()) },
            onSuccess = { routerAuthorization.toSuccessCreate() }
        )
    }
}