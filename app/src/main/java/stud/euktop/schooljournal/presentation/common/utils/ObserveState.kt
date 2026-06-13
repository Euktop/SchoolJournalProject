package stud.euktop.schooljournal.presentation.common.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import stud.euktop.domain.utils.loger.logger

inline fun <T> LifecycleOwner.observeState(
    stateFlow: StateFlow<T>,
    crossinline block: (T) -> Unit
) {
    lifecycleScope.launch {
        stateFlow.collect {
            logger?.d("ObserveState", "stateCollect", "state changed: ${it?.javaClass?.simpleName ?: "null"}")
            block(it)
        }
    }
}

inline fun <T> LifecycleOwner.observeState(
    stateFlow: StateFlow<T>,
    lifecycleOwner: LifecycleOwner = this,
    crossinline block: (T) -> Unit
) {
    observeState(stateFlow, block)
}
