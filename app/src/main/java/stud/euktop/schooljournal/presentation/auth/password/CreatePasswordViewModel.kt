package stud.euktop.schooljournal.presentation.auth.password

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

@HiltViewModel
class CreatePasswordViewModel @Inject constructor(
    private val authCoordinator: AuthCoordinator,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager
) : BaseViewModel<CreatePasswordState, Unit>() {
    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
    }

    override fun initState() = CreatePasswordState()

    fun passwordValidatorSet(value: String) {
        _state.update { it.copy(passwordValidator = it.passwordValidator.copy(value)) }
    }

    fun nextPasswordNextValidatorSet(value: String) {
        _state.update { it.copy(nextPasswordNextValidator = value) }
    }

    fun onSaveClick() {
        if (!_state.value.isNextActive()) return
        executeCoordinatorAndLoadingSync(
            block = { authCoordinator.register(state.value.passwordValidator.getValidate()) },
            onSuccess = { _ ->
                _event.emit(Unit)
            }
        )
    }
}