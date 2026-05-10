package stud.euktop.schooljournal.presentation.common.delegate

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel

class StateDelegate<STATE : BaseState<STATE>>(
    private val viewModel: BaseViewModel<STATE, *>,
    private val onStateChanged: (STATE) -> Unit
) : LifecycleAwareDelegate {

    private var stateJob: Job? = null

    override fun onViewCreated(lifecycleOwner: LifecycleOwner, savedInstanceState: Bundle?) {
        stateJob = lifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                onStateChanged(state)
            }
        }
    }

    override fun onDestroyView() {
        stateJob?.cancel()
        stateJob = null
    }
}