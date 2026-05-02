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
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.RepositoryExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager

abstract class BaseViewModel<STATE : BaseState<STATE>, EVENT : Any> : ViewModel(), RepositoryExec {
    abstract fun initState(): STATE
    protected val _state = MutableStateFlow(initState())
    val state = _state.asStateFlow()
    protected val _event = MutableSharedFlow<EVENT>()
    val event = _event.asSharedFlow()
    protected val _messageEvent = MutableSharedFlow<MessageEvent>()
    val messageEvent = _messageEvent.asSharedFlow()

    override var onError: ((CoordinatorResult.Error) -> Unit) = { result ->
        viewModelScope.launch {
            _messageEvent.emit(
                MessageEvent.Message(
                    stud.euktop.schooljournal.presentation.common.message.contract.MessageParam(
                        message = result.messageId,
                        action = { executeCoordinator.navigationManager.navigate(result.navCommand) }
                    )
                ))
        }
    }

    /**
     * Выполняет блок через [CoordinatorExec], автоматически обрабатывает ошибки:
     * - Отправляет сгенерированный [stud.euktop.schooljournal.presentation.common.navigate.NavCommand] через [NavigationManager].
     * - Публикует сообщение (SnackBar) через [MessageEvent].
     * - При успехе вызывает [onSuccess].
     */
    protected suspend inline fun <T> executeWithCoordinator(
        crossinline block: suspend () -> Result<T>,
        noinline onSuccess: suspend (T) -> Unit
    ) {
        executeCoordinator(
            block = { executeCoordinator.coordinatorExec.exec(action = { block() }) },
            onSuccess = onSuccess
        )
    }

    protected suspend inline fun <T> executeCoordinator(
        block: suspend () -> CoordinatorResult<T>,
        onSuccess: suspend (T) -> Unit
    ) {
        when (val result = block()) {
            is CoordinatorResult.Success -> onSuccess(result.result)
            is CoordinatorResult.Error -> {
                onError.invoke(result)
            }
        }
    }

    /**
     * Удобная обёртка для загрузки данных с индикацией загрузки.
     * Перед вызовом [block] включает isLoading, после — выключает.
     */
    protected suspend inline fun <T> executeWithCoordinatorAndLoading(
        crossinline block: suspend () -> Result<T>,
        noinline onSuccess: suspend (T) -> Unit
    ) {
        _state.update { it.updateIsLoading(true) }
        try {
            executeWithCoordinator(
                block,
                onSuccess
            )
        } finally {
            _state.update { it.updateIsLoading(false) }
        }
    }

    protected suspend inline fun <T> executeCoordinatorAndLoading(
        crossinline block: suspend () -> CoordinatorResult<T>,
        noinline onSuccess: suspend (T) -> Unit
    ) {
        _state.update { it.updateIsLoading(true) }
        try {
            executeCoordinator(
                block,
                onSuccess
            )
        } finally {
            _state.update { it.updateIsLoading(false) }
        }
    }

    protected inline fun <T> executeWithCoordinatorAndLoadingSync(
        crossinline block: suspend () -> Result<T>,
        noinline onSuccess: suspend (T) -> Unit
    ) = viewModelScope.launch { executeWithCoordinatorAndLoading(block, onSuccess) }

    protected inline fun <T> executeCoordinatorAndLoadingSync(
        crossinline block: suspend () -> CoordinatorResult<T>,
        noinline onSuccess: suspend (T) -> Unit
    ) = viewModelScope.launch { executeCoordinatorAndLoading(block, onSuccess) }

    protected suspend inline fun <T> CoroutineScope.executeLoadingBlock(
        block: suspend CoroutineScope.() -> T
    ): T {
        _state.update { it.updateIsLoading(true) }
        try {
            return block()
        } finally {
            _state.update { it.updateIsLoading(false) }
        }
    }

    protected inline fun <T> executeLoadingBlockSync(
        crossinline block: suspend CoroutineScope.() -> T
    ) = viewModelScope.launch { executeLoadingBlock(block) }

    protected lateinit var executeCoordinator: ExecuteCoordinator

    protected data class ExecuteCoordinator(
        val coordinatorExec: CoordinatorExec,
        val navigationManager: NavigationManager
    )
}