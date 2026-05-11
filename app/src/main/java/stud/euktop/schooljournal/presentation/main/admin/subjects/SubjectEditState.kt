// presentation/main/admin/subjects/SubjectEditState.kt
package stud.euktop.schooljournal.presentation.main.admin.subjects

import stud.euktop.domain.utils.validation.EmptyValidator
import stud.euktop.domain.utils.validation.TextThereValidator
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.contract.state.SubjectFormState

data class SubjectEditState(
    override val name: TextThereValidator = TextThereValidator(),
    override val description: EmptyValidator<String> = EmptyValidator(),
    val originalName: String = "",
    val originalDescription: String? = null,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<SubjectEditState>(), SubjectFormState {
    fun isFormValid() = name.validate()
    override fun updateLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}