package stud.euktop.schooljournal.presentation.common.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.delegate.MessageDelegate
import stud.euktop.schooljournal.presentation.common.message.contract.MessageDisplayer
import stud.euktop.schooljournal.presentation.common.message.impl.SnackBarMessages

/**
 * Упрощённая подписка на StateFlow с автоматической отменой при жизненном цикле фрагмента.
 */
inline fun <T> Fragment.observeState(
    flow: StateFlow<T>,
    crossinline block: (T) -> Unit
) {
    lifecycleScope.launch {
        flow.collect { block(it) }
    }
}

fun <VM : BaseViewModel<*, *>> Fragment.observeMessage(
    viewModel: VM,
    message: MessageDisplayer = SnackBarMessages(this.requireView(),lifecycleScope)
) {
    MessageDelegate(this.requireView(), viewLifecycleOwner, viewModel.messageEvent, message)
}

/**
 * Упрощённая подписка на SharedFlow событий.
 */
inline fun <T> Fragment.observeEvent(
    flow: SharedFlow<T>,
    crossinline block: (T) -> Unit
) {
    lifecycleScope.launch {
        flow.collect { block(it) }
    }
}