package stud.euktop.schooljournal.presentation.common.binding

import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check
import stud.euktop.schooljournal.presentation.common.utils.setup
import stud.euktop.uikit.components.input.SchJInput

fun SchJInput.bindIntField(
    focusTrack: FocusTrack,
    onValueChanged: (Int?) -> Unit
) {
    setup(focusTrack) { text -> onValueChanged(text.toIntOrNull()) }
}

fun SchJInput.checkInt(focusTrack: FocusTrack, value: Int?, isValid: Boolean = value != null && value > 0) {
    check(focusTrack, isValid)
    if (state.text != value?.toString()) {
        state = state.copy(text = value?.toString() ?: "")
    }
}