package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject
/**
 * ViewModel для детального экрана предмета.
 *
 * Назначение: загружает историю оценок по предмету.
 *
 * Функционал:
 * - Извлечение subjectId из SavedStateHandle
 * - State: marks (List<StudentSubjectMark>), isLoading
 * - loadDetails() – вызов StudentRepository.getSubjectMarks(studentId, subjectId)
 */
@HiltViewModel
class StudentSubjectDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager,
    private val studentRepository: StudentRepository
) : BaseViewModel<StudentSubjectDetailState, Unit>() {

    private val subjectId = savedStateHandle.get<Int>("subjectId") ?: 1

    override fun initState() = StudentSubjectDetailState()

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        loadDetails()
    }

    fun loadDetails() {
        executeWithCoordinatorAndLoadingSync(
            block = { studentRepository.getSubjectMarks(1, subjectId) },
            onSuccess = { marks -> _state.update { it.copy(marks = marks) } }
        )
    }
}