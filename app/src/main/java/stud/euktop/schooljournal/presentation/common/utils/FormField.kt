package stud.euktop.schooljournal.presentation.common.utils

import stud.euktop.domain.utils.validation.Validator
import stud.euktop.uikit.components.input.SchJInput

data class FormField<STATE, T : Validator<String, *>>(
    val input: SchJInput,
    val getter: (STATE) -> T,
    val setter: (String) -> Unit
)