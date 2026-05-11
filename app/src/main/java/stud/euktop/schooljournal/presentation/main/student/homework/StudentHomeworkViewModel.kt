package stud.euktop.schooljournal.presentation.main.student.homework

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.domain.repository.HomeworkRepository
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.filter.homework.AppHomeworkFilter
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class StudentHomeworkViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec,
    private val authRepository: AuthRepository,
    private val homeworkRepository: HomeworkRepository
) : BaseViewModel<StudentHomeworkState, Unit>() {

    private var studentId: Int = 0

    override fun initState() = StudentHomeworkState()

    init {
        executeCoordinator = coordinatorExec
        loadCurrentStudent()
    }

    fun filterApplied(filter: AppHomeworkFilter) {
        _state.update { it.copy(filter = filter) }
        loadHomework()
    }

    private fun loadCurrentStudent() {
        executeWithLoadingSync(
            key = "load_student",
            block = { authRepository.getCurrentUser() },
            onSuccess = { userInfo ->
                studentId = userInfo.userId
                loadHomework()
            }
        )
    }

    private fun loadHomework() {
        executeWithLoadingSync(
            key = "load_homework",
            block = {
                val filter = _state.value.filter.toDomain()
                homeworkRepository.getHomeworks(filter)
            },
            onSuccess = { list ->
                _state.update { it.copy(homeworkList = list) }
            }
        )
    }
}