package stud.euktop.schooljournal.presentation.main.student.homework

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.model.homework.HomeworkFull
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.domain.repository.HomeworkRepository
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
        executeWithResultLoadingSync(
            key = "load_student",
            block = { authRepository.getCurrentUser() },
            onSuccess = { userInfo ->
                studentId = userInfo.userId
                loadHomework()
            }
        )
    }

    private fun loadHomework() {
        viewModelScope.launch {
            withLoading("load_homework") {
                val filter = _state.value.filter.toDomain()
                val list = executeResult {
                    homeworkRepository.getHomeworks(filter)
                }.await()?.map {
                    executeResult {
                        homeworkRepository.getHomeworkFullById(it.homeworkId)
                    }
                }?.awaitAll()?.filterNotNull() ?: emptyList()
                _state.update { it.copy(homeworkList = list) }
            }
        }
    }

    fun onHomeWorkClick(homeworkFull: HomeworkFull) {
        //По факту, ничего не делает, т.к. и так уже показывается подробная информация
    }

    fun onMediaClick(mediaId: Int) {
        //TODO("Тут нужно будет добавить логику загрузки файла, но до начала не забыть создать общую утилиту для работы с файлом в common.")
    }
}