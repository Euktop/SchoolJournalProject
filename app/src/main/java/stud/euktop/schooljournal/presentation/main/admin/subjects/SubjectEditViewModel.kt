// presentation/main/admin/subjects/SubjectEditViewModel.kt
package stud.euktop.schooljournal.presentation.main.admin.subjects

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.model.common.Field
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectUpdate
import stud.euktop.domain.repository.SubjectAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.contract.action.SubjectFormActions
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import javax.inject.Inject

@HiltViewModel
class SubjectEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: SubjectAdminRepository,
    private val routerAdmin: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<SubjectEditState, Unit>(), SubjectFormActions {
    init {
        executeCoordinator = coordinatorExec
    }

    private val subjectId: Int = savedStateHandle["subjectId"] ?: 0
    private val isEditMode get() = subjectId != 0

    override fun initState() = SubjectEditState()

    init {
        if (isEditMode) loadSubject()
    }

    private fun loadSubject() {
        executeWithLoadingSync(
            key = "load",
            block = { repository.getSubject(subjectId) },
            onSuccess = { subject ->
                _state.update {
                    it.copy(
                        name = it.name.copy(subject.name),
                        description = it.description.copy(subject.description ?: ""),
                        originalName = subject.name,
                        originalDescription = subject.description
                    )
                }
            }
        )
    }

    override fun updateName(value: String) {
        _state.update { it.copy(name = it.name.copy(value)) }
    }

    override fun updateDescription(value: String) {
        _state.update { it.copy(description = it.description.copy(value)) }
    }

    fun save() {
        if (!_state.value.isFormValid()) return
        val state = _state.value

        if (isEditMode) {
            val nameChanged = state.name.value != state.originalName
            val descriptionChanged = state.description.value != state.originalDescription

            val update = SubjectUpdate(
                subjectId = subjectId,
                name = Field(state.name.getValidate(), nameChanged),
                description = Field(
                    state.description.getValidate().takeIf { it.isNotBlank() },
                    descriptionChanged
                )
            )
            executeWithLoadingSync(
                key = "save",
                block = { repository.updateSubject(update) },
                onSuccess = { routerAdmin.toBack() }
            )
        } else {
            val subject = Subject(
                subjectId = 0,
                name = state.name.getValidate(),
                description = state.description.getValidate().takeIf { it.isNotBlank() }
            )
            executeWithLoadingSync(
                key = "save",
                block = { repository.addSubject(subject) },
                onSuccess = { routerAdmin.toBack() }
            )
        }
    }

    fun cancel() {
        viewModelScope.launch {
            routerAdmin.toBack()
        }
    }
}