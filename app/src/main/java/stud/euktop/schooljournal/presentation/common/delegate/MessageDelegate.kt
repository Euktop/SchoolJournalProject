package stud.euktop.schooljournal.presentation.common.delegate

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.message.MessageEvent
import stud.euktop.schooljournal.presentation.common.message.contract.MessageDisplayer
import stud.euktop.schooljournal.presentation.common.message.impl.SnackBarMessages

class MessageDelegate(
    private val rootView: View,
    private val lifecycleOwner: LifecycleOwner,
    private val messageEventFlow: SharedFlow<MessageEvent>,
    private val messageDisplayer: MessageDisplayer = SnackBarMessages(rootView)
) {

    init {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                messageEventFlow.collect { event ->
                    when (event) {
                        is MessageEvent.Message -> messageDisplayer.message(event.param)
                    }
                }
            }
        }
    }
}