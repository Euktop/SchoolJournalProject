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
import stud.euktop.schooljournal.presentation.common.coordinator.HomeworkCoordinator
import stud.euktop.schooljournal.presentation.common.filter.homework.AppHomeworkFilter
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class StudentHomeworkViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec,
    private val authRepository: AuthRepository,
    private val homeworkRepository: HomeworkRepository,
    private val homeworkCoordinator: HomeworkCoordinator
) : BaseViewModel<StudentHomeworkState, StudentHomeworkEvent>() {

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
        _event.tryEmit(StudentHomeworkEvent.ShowHomeworkDetail(homeworkFull))
    }

    fun onMediaClick(mediaId: Int) {
        executeCoordinatorResultLoadingBlockSync(
            key = "download_media",
            block = { homeworkCoordinator.downloadMedia(mediaId) }
        ) { file ->
            _event.tryEmit(StudentHomeworkEvent.DownloadMediaFile(file))
        }
    }
}