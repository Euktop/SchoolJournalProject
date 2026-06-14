package stud.euktop.schooljournal.presentation.main.teacher.dashboard

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.coordinator.TeacherCoordinator
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterTeacher
import javax.inject.Inject

@HiltViewModel
class TeacherDashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val teacherCoordinator: TeacherCoordinator,
    private val routerTeacher: RouterTeacher,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<TeacherDashboardState, TeacherDashboardEvent>() {

    override fun initState() = TeacherDashboardState()

    init {
        executeCoordinator = coordinatorExec
        loadData()
    }

    private fun loadData() {
        loadProfile()
        loadNextLesson()
        loadStatistics()
    }

    private fun loadProfile() {
        executeWithResultLoadingSync(
            key = "profile",
            block = { authRepository.getCurrentUser() },
            onSuccess = { user ->
                val name = "${user.firstName} ${user.lastName}".trim()
                _state.update { it.copy(teacherName = name.ifEmpty { "Учитель" }) }
            }
        )
    }

    private fun loadNextLesson() {
        executeCoordinatorResultLoadingBlockSync(
            key = "nextLesson",
            block = {
                val user = authRepository.getCurrentUser().getOrNull()
                if (user != null) teacherCoordinator.getNextLesson(user.userId)
                else CoordinatorResult.Success(null)
            },
            onSuccess = { lesson ->
                val info = lesson?.let {
                    "${it.startTime}, ${it.topic?.take(20) ?: "урок"}"
                } ?: ""
                _state.update { it.copy(nextLessonInfo = info) }
            }
        )
    }

    private fun loadStatistics() {
        executeCoordinatorResultLoadingBlockSync(
            key = "stats",
            block = {
                val user = authRepository.getCurrentUser().getOrNull()
                if (user != null) teacherCoordinator.getTeacherStatistics(user.userId)
                else CoordinatorResult.Success(TeacherStatistics(0, 0))
            },
            onSuccess = { stats ->
                _state.update {
                    it.copy(
                        lessonsCount = stats.lessonsTodayCount
                    )
                }
            }
        )
    }

    // Навигация: события для табов — фрагмент обработает через MainController
    fun onScheduleClick() { viewModelScope.launch { _event.emit(TeacherDashboardEvent.SwitchToSchedule) } }
    fun onMyClassesClick() { viewModelScope.launch { _event.emit(TeacherDashboardEvent.SwitchToClasses) } }
    fun onLessonsListClick() { viewModelScope.launch { _event.emit(TeacherDashboardEvent.SwitchToClasses) } }
    fun onGradingClick() { viewModelScope.launch { _event.emit(TeacherDashboardEvent.SwitchToClasses) } }
    fun onHomeworkListClick() { viewModelScope.launch { _event.emit(TeacherDashboardEvent.SwitchToHomework) } }

    fun onCreateHomeworkClick() = routerTeacher.toTeacherHomeworkEdit()
    fun onAnalyticsClick() = routerTeacher.toTeacherAnalytics()
    fun onSettingsClick() { /* реализовать по необходимости */ }
}

sealed class TeacherDashboardEvent {
    object SwitchToSchedule : TeacherDashboardEvent()
    object SwitchToClasses : TeacherDashboardEvent()
    object SwitchToHomework : TeacherDashboardEvent()
}