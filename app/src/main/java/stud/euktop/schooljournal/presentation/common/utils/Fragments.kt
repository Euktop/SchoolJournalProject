package stud.euktop.schooljournal.presentation.common.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.delegate.MessageDelegate
import stud.euktop.schooljournal.presentation.common.message.contract.MessageDisplayer
import stud.euktop.schooljournal.presentation.common.message.impl.SnackBarMessages

inline fun <T> Fragment.observeState(
    flow: StateFlow<T>,
    crossinline block: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
            flow.collect { block(it) }
        }
    }
}

inline fun <T> Fragment.observeEvent(
    flow: SharedFlow<T>,
    crossinline block: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
            flow.collect { block(it) }
        }
    }
}

fun <VM : BaseViewModel<*, *>> Fragment.observeMessage(
    viewModel: VM,
    message: MessageDisplayer = SnackBarMessages(requireView(), viewLifecycleOwner.lifecycleScope)
) {
    MessageDelegate(requireView(), viewLifecycleOwner, viewModel.messageEvent, message)
}