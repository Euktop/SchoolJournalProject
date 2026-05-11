package stud.euktop.schooljournal.presentation.common.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

inline fun <T> LifecycleOwner.observeState(
    stateFlow: StateFlow<T>,
    crossinline block: (T) -> Unit
) {
    lifecycleScope.launch {
        stateFlow.collect { block(it) }
    }
}

inline fun <T> LifecycleOwner.observeState(
    stateFlow: StateFlow<T>,
    lifecycleOwner: LifecycleOwner = this,
    crossinline block: (T) -> Unit
) {
    observeState(stateFlow, block)
}