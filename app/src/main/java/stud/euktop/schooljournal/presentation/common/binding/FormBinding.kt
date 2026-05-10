package stud.euktop.schooljournal.presentation.common.binding

import androidx.fragment.app.Fragment
import stud.euktop.domain.utils.validation.ValidatorInterface
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check
import stud.euktop.schooljournal.presentation.common.utils.observeState
import stud.euktop.schooljournal.presentation.common.utils.setup
import stud.euktop.uikit.components.input.SchJInput

inline fun <STATE : BaseState<STATE>> Fragment.bindForm(
    focusTrack: FocusTrack,
    viewModel: BaseViewModel<STATE, *>,
    crossinline builder: FormBindingScope<STATE>.() -> Unit
) {
    val scope = FormBindingScope<STATE>(focusTrack)
    scope.builder()
    observeState(viewModel.state) { state ->
        scope.updateFields(state)
    }
}

class FormBindingScope<STATE : BaseState<STATE>>(
    private val focusTrack: FocusTrack
) {
    private val fields = mutableListOf<(STATE) -> Unit>()

    fun field(
        input: SchJInput,
        getter: (STATE) -> ValidatorInterface<String>,
        setter: (String) -> Unit
    ) {
        input.setup(focusTrack, setter)
        fields.add { state ->
            input.check(focusTrack, getter(state))
        }
    }

    fun updateFields(state: STATE) {
        fields.forEach { it(state) }
    }
}