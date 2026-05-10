package stud.euktop.schooljournal.presentation.common.delegate

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel

class LoadingDelegate<STATE : BaseState<STATE>>(
    private val viewModel: BaseViewModel<STATE, *>,
    private val lifecycleOwner: LifecycleOwner,
    private val loadingMapSelector: (STATE) -> Map<String, Boolean> = { it.loadingMap }
) {

    private val subscriptions = mutableMapOf<String, MutableList<(Boolean) -> Unit>>()
    private var previousMap: Map<String, Boolean>? = null

    init {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state
                    .map { loadingMapSelector(it) }
                    .distinctUntilChanged()
                    .collect { currentMap ->
                        val oldMap = previousMap ?: emptyMap()
                        subscriptions.keys.forEach { key ->
                            val oldValue = oldMap[key] == true
                            val newValue = currentMap[key] == true
                            if (oldValue != newValue) {
                                subscriptions[key]?.forEach { it(newValue) }
                            }
                        }
                        previousMap = currentMap
                    }
            }
        }
    }

    fun observeLoading(key: String, callback: (Boolean) -> Unit) {
        subscriptions.getOrPut(key) { mutableListOf() }.add(callback)
    }
}