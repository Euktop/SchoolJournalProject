package stud.euktop.schooljournal.presentation.common.binding

import stud.euktop.uikit.components.input.SchJInput

fun SchJInput.syncIntValue(value: Int?) {
    if (state.text != value?.toString()) {
        state = state.copy(text = value?.toString() ?: "")
    }
}