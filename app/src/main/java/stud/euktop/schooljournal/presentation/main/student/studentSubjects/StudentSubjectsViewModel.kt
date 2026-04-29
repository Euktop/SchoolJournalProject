package stud.euktop.schooljournal.presentation.main.student.studentSubjects

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject
/**
 * ViewModel для экрана предметов ученика.
 *
 * Назначение: загружает сводку по предметам (средний балл, итог) для ученика.
 *
 * Функционал:
 * - State: subjects (List<StudentSubjectSummary>), isLoading
 * - loadSubjects() – вызов StudentRepository.getSubjectsSummary(studentId)
 * - studentId можно пока захардкодить (например, 1) или получать из профиля
 */
@HiltViewModel
class StudentSubjectsViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager
) : BaseViewModel<StudentSubjectsState, Unit>() {
    override fun initState() = StudentSubjectsState()

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        loadSubjects()
    }

    fun loadSubjects() {
        executeWithCoordinatorAndLoadingSync(
            block = { studentRepository.getSubjectsSummary(1) },
            onSuccess = { summaries -> _state.update { it.copy(subjects = summaries) } }
        )
    }
}