package stud.euktop.schooljournal.presentation.common.utils

import android.view.View
import stud.euktop.domain.utils.validation.Validator
import stud.euktop.uikit.components.input.SchJInput

inline fun SchJInput.setup(
    focusTrack: FocusTrack,
    crossinline action: (String) -> Unit
) {
    listener = { action(it) }
}

inline fun SchJInput.setup(
    focusTrack: FocusTrack,
    crossinline action: (String) -> Unit,
    crossinline focusLost: (() -> Unit)
) {
    this.setup(focusTrack, action)
    focusTrack.setFocusListener(this) { focusLost() }
}

fun SchJInput.check(focusTrack: FocusTrack, validate: Boolean) {
    this.state = this.state.copy(isErrorVisible = focusTrack.isTouched(this) && !validate)
}

fun SchJInput.check(focusTrack: FocusTrack, validate: Validator<String, *>) {
    check(focusTrack, validate.validate())
    state = state.copy(text = validate.value ?: "")
}