package stud.euktop.schooljournal.presentation.auth.login

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
) : BaseViewModel<LoginState, Unit>() {
    override fun initState() = LoginState()

    fun emailSet(email: String) {
        _state.update { it.copy(emailValidator = it.emailValidator.copy(email)) }
    }

    fun passwordSet(password: String) {
        _state.update { it.copy(passwordValidator = it.passwordValidator.copy(password)) }
    }
}