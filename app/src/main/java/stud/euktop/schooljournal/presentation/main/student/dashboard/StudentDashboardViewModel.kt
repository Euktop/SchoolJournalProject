package stud.euktop.schooljournal.presentation.main.student.dashboard

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.domain.repository.HomeworkRepository
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class StudentDashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val studentRepository: StudentRepository,
    private val homeworkRepository: HomeworkRepository,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<StudentDashboardState, Unit>() {

    override fun initState() = StudentDashboardState()

    init {
        executeCoordinator = coordinatorExec
        loadStudentInfo()
    }

    private fun loadStudentInfo() {
        executeWithLoadingSync(
            key = "load_student",
            block = { authRepository.getCurrentUser() },
            onSuccess = { user ->
                val fullName = "${user.firstName} ${user.lastName}".trim()
                _state.update { it.copy(studentName = fullName.ifEmpty { "Ученик" }) }
            }
        )
    }
}