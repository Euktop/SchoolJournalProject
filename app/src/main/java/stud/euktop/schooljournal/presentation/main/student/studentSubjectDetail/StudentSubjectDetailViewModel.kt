package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class StudentSubjectDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val studentRepository: StudentRepository,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<StudentSubjectDetailState, Unit>() {

    private val subjectId = savedStateHandle.get<Int>("subjectId") ?: 0

    override fun initState() = StudentSubjectDetailState()

    init {
        executeCoordinator = coordinatorExec
        loadDetails()
    }

    private fun loadDetails() {
        executeWithLoadingSync(
            key = "load_marks",
            block = { studentRepository.getSubjectMarks(subjectId) },
            onSuccess = { marks -> _state.update { it.copy(marks = marks) } }
        )
    }
}