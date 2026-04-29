package stud.euktop.schooljournal.presentation.auth.login

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

/**
 * ViewModel для экрана авторизации.
 *
 * Назначение: управляет состоянием полей ввода, валидацией и процессом входа.
 *
 * Функционал:
 * - Хранит состояние (LoginState): emailValidator, passwordValidator, isLoading
 * - Методы обновления email и пароля
 * - Метод onLoginClick() – вызывает AuthCoordinator.login
 * - При успехе отправляет событие NavigateToMain для перехода в главное меню
 *
 * @see LoginFragment
 * @see LoginState
 * @see LoginEvent
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authCoordinator: AuthCoordinator,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager
) : BaseViewModel<LoginState, LoginEvent>() {
    override fun initState() = LoginState()

    fun emailSet(email: String) {
        _state.update { it.copy(emailValidator = it.emailValidator.copy(email)) }
    }

    fun passwordSet(password: String) {
        _state.update { it.copy(passwordValidator = it.passwordValidator.copy(password)) }
    }

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
    }

    fun onLoginClick() {
        if (!_state.value.isButtonActive()) {
            _state.update { it.updateIsLoading(!it.isLoading) }
            _state.update { it.updateIsLoading(!it.isLoading) }
            return
        }
        executeCoordinatorAndLoadingSync(
            block = {
                authCoordinator.login(
                    state.value.emailValidator.value ?: "",
                    state.value.passwordValidator.value ?: ""
                )
            },
            onSuccess = { profile ->
                _event.emit(LoginEvent.NavigateToMain)
            }
        )
    }
}