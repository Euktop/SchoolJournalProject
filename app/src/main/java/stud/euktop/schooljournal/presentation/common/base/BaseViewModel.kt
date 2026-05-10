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
import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam
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
                    MessageParam(
                        message = result.messageId,
                        action = { executeCoordinator.navigationManager.navigate(result.navCommand) }
                    )
                )
            )
        }
    }

    // ========== Управление загрузкой через loadingMap ==========

    protected fun setLoading(key: String, isLoading: Boolean) {
        _state.update { it.withLoading(key, isLoading) }
    }

    protected fun startLoading(key: String) = setLoading(key, true)
    protected fun stopLoading(key: String) = setLoading(key, false)

    protected suspend fun <T> withLoading(key: String, block: suspend () -> T): T {
        startLoading(key)
        return try {
            block()
        } finally {
            stopLoading(key)
        }
    }

    // Вариант executeWithCoordinator, который автоматически управляет загрузкой по ключу
    protected suspend inline fun <T> executeWithLoading(
        key: String,
        crossinline block: suspend () -> Result<T>,
        noinline onSuccess: suspend (T) -> Unit
    ) {
        startLoading(key)
        try {
            executeWithCoordinator(block, onSuccess)
        } finally {
            stopLoading(key)
        }
    }

    protected inline fun <T> executeWithLoadingSync(
        key: String,
        crossinline block: suspend () -> Result<T>,
        noinline onSuccess: suspend (T) -> Unit
    ) = viewModelScope.launch {
        executeWithLoading(key, block, onSuccess)
    }

    // ========== Старые методы с isLoading – удалены. ==========
    // (были executeWithCoordinatorAndLoading и т.п.)

    // ========== Общая обёртка для CoordinatorExec ==========
    protected suspend inline fun <T> executeWithCoordinator(
        crossinline block: suspend () -> Result<T>,
        noinline onSuccess: suspend (T) -> Unit
    ) {
        executeCoordinator(
            block = { executeCoordinator.coordinatorExec.exec { block() } },
            onSuccess = onSuccess
        )
    }

    protected suspend inline fun <T> executeCoordinator(
        block: suspend () -> CoordinatorResult<T>,
        onSuccess: suspend (T) -> Unit
    ) {
        when (val result = block()) {
            is CoordinatorResult.Success -> onSuccess(result.result)
            is CoordinatorResult.Error -> onError.invoke(result)
        }
    }

    // ========== Хелпер для блоков без Coordinator ==========
    protected suspend inline fun <T> CoroutineScope.executeLoadingBlock(
        key: String,
        crossinline block: suspend CoroutineScope.() -> CoordinatorResult<T>,
        crossinline onSuccess: suspend (T) -> Unit
    ) = withLoading(key) { executeCoordinator({ block() }, onSuccess) }

    protected inline fun <T> executeLoadingBlockSync(
        key: String,
        crossinline block: suspend CoroutineScope.() -> CoordinatorResult<T>,
        crossinline onSuccess: suspend (T) -> Unit
    ) = viewModelScope.launch { executeLoadingBlock(key, block, onSuccess) }

    // ========== Координатор (инициализируется извне) ==========
    protected lateinit var executeCoordinator: ExecuteCoordinator

    protected data class ExecuteCoordinator(
        val coordinatorExec: CoordinatorExec,
        val navigationManager: NavigationManager
    )
}