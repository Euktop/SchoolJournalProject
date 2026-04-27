package stud.euktop.schooljournal.presentation.auth.password

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class CreatePasswordViewModel @Inject constructor() : BaseViewModel<CreatePasswordState, Unit>() {
    override fun initState() = CreatePasswordState()

    fun passwordValidatorSet(value: String) {
        _state.update { it.copy(passwordValidator = it.passwordValidator.copy(value)) }
    }

    fun nextPasswordNextValidatorSet(value: String) {
        _state.update { it.copy(nextPasswordNextValidator = value) }
    }
}