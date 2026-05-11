package stud.euktop.schooljournal.presentation.common.contract.state

import stud.euktop.domain.utils.validation.TextThereValidator

interface ClassFormState {
    val grade: Int?
    val letter: TextThereValidator
    val academicYearStart: Int?
    val academicYearEnd: Int?
}