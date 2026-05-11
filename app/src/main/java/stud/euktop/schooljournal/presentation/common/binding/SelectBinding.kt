package stud.euktop.schooljournal.presentation.common.binding

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.adapter.ListSelectAdapter
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.utils.observeState
import stud.euktop.uikit.components.input.select.def.SchJSelect

inline fun <STATE : BaseState<STATE>, T : Any> Fragment.bindSelect(
    select: SchJSelect,
    viewModel: BaseViewModel<STATE, *>,
    crossinline getter: (STATE) -> T?,
    noinline toText: (T?) -> String,
    noinline onSelected: (T?) -> Unit,
    items: List<T> = emptyList()
) {
    val adapter = ListSelectAdapter(
        toText = toText,
        onItemSelected = onSelected
    )
    adapter.submitList(items)
    select.attach(adapter, adapter, childFragmentManager)

    observeState(viewModel.state) { state ->
        val value = getter(state)
        select.state = select.state.copy(selectText = toText(value))
    }
}

inline fun <STATE : BaseState<STATE>, T : Any> Fragment.bindSelectLive(
    select: SchJSelect,
    viewModel: BaseViewModel<STATE, *>,
    crossinline getter: (STATE) -> T?,
    noinline toText: (T?) -> String,
    noinline onSelected: (T?) -> Unit,
    itemsFlow: Flow<List<T>>,
) {
    val adapter = ListSelectAdapter(
        toText = toText,
        onItemSelected = onSelected
    )
    select.attach(adapter, adapter, childFragmentManager)

    lifecycleScope.launch {
        itemsFlow.collect { items ->
            adapter.submitList(items)
        }
    }

    observeState(viewModel.state) { state ->
        val value = getter(state)
        select.state = select.state.copy(selectText = toText(value))
    }
}