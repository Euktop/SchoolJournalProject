package stud.euktop.schooljournal.presentation.common.utils

import stud.euktop.domain.utils.validation.Validator
import stud.euktop.uikit.components.input.SchJInput

inline fun SchJInput.setup(
    focusTrack: FocusTrack,
    crossinline action: (String) -> Unit,
    crossinline focusLost: (() -> Unit)
) {
    editText?.let {
        focusTrack.setFocusListener(it, id) {
            focusLost()
        }
    }
    listener = { action(it) }
}

inline fun SchJInput.setup(
    focusTrack: FocusTrack,
    crossinline action: (String) -> Unit
) {
    editText?.let {
        focusTrack.setFocusListener(it, id)
    }
    listener = { action(it) }
}


fun SchJInput.check(focusTrack: FocusTrack, validate: Boolean) {
    this.state = this.state.copy(isErrorVisible = focusTrack.isTouched(this) && !validate)
}

fun SchJInput.check(focusTrack: FocusTrack, validate: Validator<String, *>) {
    check(focusTrack, validate.validate())
    val text = validate.value ?: ""
    if (state.text != text)
        state = state.copy(text = validate.value ?: "")
}
