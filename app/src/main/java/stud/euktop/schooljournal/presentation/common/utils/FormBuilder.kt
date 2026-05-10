package stud.euktop.schooljournal.presentation.common.utils

import stud.euktop.domain.utils.validation.Validator
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.uikit.components.input.SchJInput

class FormBuilder<STATE : BaseState<STATE>>(
    private val focusTrack: FocusTrack,
    private val state: STATE
) {
    private val fields = mutableListOf<FormField<STATE, *>>()

    fun field(
        input: SchJInput,
        getter: (STATE) -> Validator<String, *>,
        setter: (String) -> Unit
    ): FormBuilder<STATE> {
        input.setup(focusTrack, setter)
        fields.add(FormField(input, getter, setter))
        return this
    }

    fun bindToState(updateState: (STATE) -> Unit) {
    }

    fun updateFields(state: STATE) {
        fields.forEach { field ->
            val validator = field.getter(state)
            field.input.check(focusTrack, validator)
        }
    }
}