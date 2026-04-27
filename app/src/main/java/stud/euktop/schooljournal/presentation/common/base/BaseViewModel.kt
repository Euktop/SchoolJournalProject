package stud.euktop.schooljournal.presentation.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.message.MessageEvent

abstract class BaseViewModel<STATE : BaseState<STATE>, EVENT : Any> : ViewModel() {
    abstract fun initState(): STATE
    protected val _state = MutableStateFlow(initState())
    val state = _state.asStateFlow()
    protected val _event = MutableSharedFlow<EVENT>()
    val event = _event.asSharedFlow()
    protected val _messageEvent = MutableSharedFlow<MessageEvent>()
    val messageEvent = _messageEvent.asSharedFlow()

    protected inline fun <T> execSync(
        crossinline block: CoroutineScope.() -> Result<T>,
        exec: ExecClass<T>? = null
    ) {
        viewModelScope.launch {
            exec(block, exec)
        }
    }

    protected suspend inline fun <T> CoroutineScope.exec(
        block: CoroutineScope.() -> Result<T>,
        exec: ExecClass<T>? = null
    ) {
        block().fold(
            onSuccess = { exec?.success?.invoke(it) },
            onFailure = { exec?.failure?.invoke() })
    }

    protected inline fun <T> blockLoading(
        crossinline block: CoroutineScope.() -> Result<T>,
        exec: ExecClass<T>? = null
    ) {
        _state.update { it.updateIsLoading(true) }
        try {
            execSync(block, exec)
        } finally {
            _state.update { it.updateIsLoading(false) }
        }
    }

    protected data class ExecClass<T>(
        val success: (suspend (T) -> Unit)? = null,
        val failure: (suspend () -> Unit)? = null
    )
}