package stud.euktop.schooljournal.presentation.common.base

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import stud.euktop.uikit.components.input.SchJInput


abstract class BaseFilterDialog<VM : BaseFilterViewModel, T>(
    protected var initialFilter: T,
    protected val onFilterApplied: (T) -> Unit,
    protected val onError: (CoordinatorResult.Error) -> Unit
) :
    stud.euktop.uikit.components.filter.BaseFilterDialog<T>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewLifecycleOwner.lifecycleScope.launch {
                    setups.forEach {
                        setup(it)
                    }
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.error.collect { onError(it) }
                    }
                }
            }
        }
    }

    protected abstract val setups: List<suspend () -> Unit>

    private suspend inline fun setup(crossinline action: suspend () -> Unit) {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            action()
        }
    }

    override fun onApply(filter: T) {
        onFilterApplied(filter)
    }

    abstract val viewModel: VM


    protected object Utils {
        fun inputClears(inputs: List<SchJInput>) {
            inputs.forEach { it.state = it.state.copy(text = "") }
        }

        fun inputText(inputs: List<SchJInput>) = ArrayDeque(
            inputs.map { it -> it.state.text.takeIf { it.isNotEmpty() } })

        fun inputInit(
            container: LinearLayout,
            vararg inputs: Pair<Int, String?>
        ) = inputs.map {
            FilterFieldBuilder.addText(
                container,
                container.resources.getString(it.first),
                it.second ?: ""
            )
        }

    }
}
