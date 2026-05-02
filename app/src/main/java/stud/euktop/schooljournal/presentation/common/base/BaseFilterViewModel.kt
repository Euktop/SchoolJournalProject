package stud.euktop.schooljournal.presentation.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec

abstract class BaseFilterViewModel(
    protected val coordinatorExec: CoordinatorExec
) : ViewModel() {
    protected val _error = MutableSharedFlow<CoordinatorResult.Error>()
    val error: SharedFlow<CoordinatorResult.Error> = _error

    protected suspend inline fun <T> exec(
        crossinline action: suspend () -> Result<T>, crossinline onSuccess: (T) -> Unit
    ) {
        val result = coordinatorExec.exec {
            action()
        }
        when (result) {
            is CoordinatorResult.Error -> _error.emit(result)
            is CoordinatorResult.Success<T> -> onSuccess(result.result)
        }
    }

    protected inline fun <T> execSync(
        crossinline action: suspend () -> Result<T>,
        crossinline onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            exec(action, onSuccess)
        }
    }
}