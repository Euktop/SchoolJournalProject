package stud.euktop.schooljournal.presentation.main.student.homework

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.homework.HomeworkFilter2
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.domain.repository.HomeworkRepository
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

@HiltViewModel
class StudentHomeworkViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager,
    private val authRepository: AuthRepository,
    private val studentRepository: StudentRepository,
    private val homeworkRepository: HomeworkRepository
) : BaseViewModel<StudentHomeworkState, StudentHomeworkEvent>() {

    private var studentId: Int = 0

    override fun initState() = StudentHomeworkState()

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        loadCurrentStudent()
    }

    fun filterApplied(filter: HomeworkFilter2) {
        _state.update { it.copy(filter = filter) }
        loadCurrentStudent()
    }

    private fun loadCurrentStudent() {
        executeLoadingBlockSync {
            executeWithCoordinator(
                block = { authRepository.getCurrentUser() },
                onSuccess = { userInfo ->
                    studentId = userInfo.userId
                    loadStudentClassAndHomework()
                }
            )
        }
    }

    private suspend fun loadStudentClassAndHomework() {
        executeWithCoordinator(
            block = { studentRepository.getStudentClassId(studentId) },
            onSuccess = { classId ->
                val filter = _state.value.filter.copy(classId = classId)
                _state.update { it.copy(filter = filter) }
                loadHomeworkList(filter)
            }
        )
    }

    private suspend fun loadHomeworkList(filter: HomeworkFilter2) {
        executeWithCoordinator(
            block = { homeworkRepository.getHomeworks(filter) },
            onSuccess = { list ->
                _state.update { it.copy(homeworkList = list) }
            }
        )
    }
}