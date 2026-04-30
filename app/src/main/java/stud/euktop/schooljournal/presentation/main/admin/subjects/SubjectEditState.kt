// presentation/main/admin/subjects/SubjectEditState.kt
package stud.euktop.schooljournal.presentation.main.admin.subjects

import stud.euktop.domain.utils.validation.TextThereValidator
import stud.euktop.domain.utils.validation.Validator
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class SubjectEditState(
    override val isLoading: Boolean = false,
    val subjectId: Int = 0,
    val name: TextThereValidator = TextThereValidator(),
    val description: String = ""
) : BaseState<SubjectEditState>() {

    fun isEditMode() = subjectId != 0

    fun isFormValid(): Boolean = Validator.isAllValidate(name)

    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}