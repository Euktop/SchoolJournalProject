// presentation/main/admin/subjects/SubjectEditViewModel.kt
package stud.euktop.schooljournal.presentation.main.admin.subjects

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.Subject
import stud.euktop.domain.repository.AdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

@HiltViewModel
class SubjectEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager,
    private val adminRepository: AdminRepository
) : BaseViewModel<SubjectEditState, SubjectEditEvent>() {

    companion object {
        const val KEY_SUBJECT_ID = "subjectId"
    }

    private val subjectId: Int = savedStateHandle[KEY_SUBJECT_ID] ?: 0

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        if (subjectId != 0) loadSubject()
    }

    override fun initState() = SubjectEditState(subjectId = subjectId)

    private fun loadSubject() {
        executeWithCoordinatorAndLoadingSync(
            block = { adminRepository.getSubject(subjectId) },
            onSuccess = { subject ->
                _state.update {
                    it.copy(
                        subjectId = subject.subjectId,
                        name = it.name.copy(subject.name),
                        description = subject.description ?: ""
                    )
                }
            }
        )
    }

    fun updateName(value: String) {
        _state.update { it.copy(name = it.name.copy(value)) }
    }

    fun updateDescription(value: String) {
        _state.update { it.copy(description = value) }
    }

    fun save() {
        val state = _state.value
        if (!state.isFormValid()) return

        val subject = Subject(
            subjectId = state.subjectId,
            name = state.name.getValidate(),
            description = state.description.takeIf { it.isNotBlank() }
        )


        executeWithCoordinatorAndLoadingSync(
            block = {
                if (state.isEditMode()) {
                    adminRepository.updateSubject(subject)
                } else {
                    adminRepository.addSubject(subject)
                }
            },
            onSuccess = { _event.emit(SubjectEditEvent.NavigateBack) }
        )
    }
}