package stud.euktop.schooljournal.presentation.common.utils

import stud.euktop.domain.utils.validation.Validator
import stud.euktop.domain.utils.validation.ValidatorInterface
import stud.euktop.uikit.components.input.SchJInput
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag

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
    try {
        logger?.d(this.toSimpleTag(), "inputSetup", "id=$id")
    } catch (_: Throwable) {
    }
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

fun SchJInput.check(focusTrack: FocusTrack, validate: ValidatorInterface<String>) {
    check(focusTrack, validate.validate())
    val text = validate.value ?: ""
    if (state.text != text)
        state = state.copy(text = validate.value ?: "")
}

fun inputChecks(focusTrack: FocusTrack, vararg inputs: Pair<SchJInput, Validator<String, *>>) =
    inputs.forEach { it.first.check(focusTrack, it.second) }
