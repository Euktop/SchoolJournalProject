package stud.euktop.schooljournal.presentation.auth.login

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterMain
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
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authCoordinator: AuthCoordinator,
    private val routerMain: RouterMain
) : BaseViewModel<LoginState, Unit>() {

    override fun initState() = LoginState()

    fun emailSet(email: String) {
        _state.update { it.copy(email = it.email.copy(email)) }
    }

    fun passwordSet(password: String) {
        _state.update { it.copy(password = it.password.copy(password)) }
    }

    fun onLoginClick() {
        if (!_state.value.isButtonActive()) return
        executeLoadingBlockSync(
            key = "login",
            block = {
                authCoordinator.login(
                    email = _state.value.email.getValidate(),
                    password = _state.value.password.getValidate()
                )
            }, { routerMain.toMain() }
        )
    }
}