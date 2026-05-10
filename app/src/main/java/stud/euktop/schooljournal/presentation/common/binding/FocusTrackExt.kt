package stud.euktop.schooljournal.presentation.common.binding

import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.uikit.components.input.SchJInput

fun FocusTrack.register(vararg inputs: SchJInput) {
    inputs.forEach { input ->
        input.editText?.let { editText ->
            setFocusListener(editText, input.id)
        }
    }
}