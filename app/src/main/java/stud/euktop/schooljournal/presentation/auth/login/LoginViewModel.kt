package stud.euktop.schooljournal.presentation.auth.login

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.message.MessageEvent
import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAuth
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
    private val routerMain: RouterMain,
    private val routerAuth: RouterAuth
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

    fun onRegClick() {
        viewModelScope.launch {
            routerAuth.toCreateProfile()
        }
    }

    fun onForgetClick() {
        viewModelScope.launch {
            _messageEvent.emit(
                MessageEvent.Message(
                    MessageParam(
                        message = R.string.it_is_not_possible_to_reset_the_password_yet,
                        action = {},
                        dismiss = {}
                    )
                )
            )
        }
    }
}