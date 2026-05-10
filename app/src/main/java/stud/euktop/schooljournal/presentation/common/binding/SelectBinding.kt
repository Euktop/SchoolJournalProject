package stud.euktop.schooljournal.presentation.common.binding

import androidx.fragment.app.Fragment
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.utils.observeState
import stud.euktop.uikit.components.input.select.ListSafe
import stud.euktop.uikit.components.input.select.def.SchJSelect

inline fun <STATE : BaseState<STATE>, T : Any> Fragment.bindSelect(
    select: SchJSelect,
    viewModel: BaseViewModel<STATE, *>,
    crossinline getter: (STATE) -> T?,
    crossinline toText: (T?) -> String,
    crossinline onSelected: (T?) -> Unit,
    items: List<T> = emptyList()
) {
    val listSafe = ListSafe(
        values = items,
        toText = { toText(it) },
        onClick = { value, _ -> onSelected(value) }
    )
    select.RegisterList(listSafe).register(childFragmentManager)

    observeState(viewModel.state) { state ->
        val value = getter(state)
        select.state = select.state.copy(selectText = toText(value))
    }
}