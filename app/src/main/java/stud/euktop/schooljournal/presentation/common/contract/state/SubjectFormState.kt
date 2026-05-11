package stud.euktop.schooljournal.presentation.common.contract.state

import stud.euktop.domain.utils.validation.EmptyValidator
import stud.euktop.domain.utils.validation.TextThereValidator

interface SubjectFormState {
    val name: TextThereValidator
    val description: EmptyValidator<String>
}