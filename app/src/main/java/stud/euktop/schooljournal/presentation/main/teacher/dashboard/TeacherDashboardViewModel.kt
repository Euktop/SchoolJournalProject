package stud.euktop.schooljournal.presentation.main.teacher.dashboard

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterTeacher
import javax.inject.Inject

@HiltViewModel
class TeacherDashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val routerTeacher: RouterTeacher,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<TeacherDashboardState, Unit>() {

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
        executeWithLoadingSync(
            key = "profile",
            block = { authRepository.getCurrentUser() },
            onSuccess = { user ->
                val name = "${user.firstName} ${user.lastName}".trim()
                _state.update { it.copy(teacherName = name.ifEmpty { "Учитель" }) }
            }
        )
    }

    private fun loadNextLesson() {
        // TODO: Реальная реализация - получить следующий урок учителя
        executeWithLoadingSync(
            key = "nextLesson",
            block = { Result.success("") },
            onSuccess = { _ ->
                _state.update { it.copy(nextLessonInfo = "10:30, 5А (Алгебра)") }
            }
        )
    }

    private fun loadStatistics() {
        // TODO: Реальная реализация - получить статистику
        executeWithLoadingSync(
            key = "stats",
            block = { Result.success(Unit) },
            onSuccess = { _ ->
                _state.update {
                    it.copy(
                        lessonsCount = 4,
                        classesCount = 3
                    )
                }
            }
        )
    }

    fun onScheduleClick() = routerTeacher.toTeacherSchedule()
    fun onMyClassesClick() = routerTeacher.toTeacherClasses()
    fun onLessonsListClick() = routerTeacher.toTeacherClasses()
    fun onGradingClick() = routerTeacher.toTeacherClasses()
    fun onHomeworkListClick() = routerTeacher.toTeacherHomeworkList()
    fun onCreateHomeworkClick() = routerTeacher.toTeacherHomeworkEdit()
    fun onAnalyticsClick() = routerTeacher.toTeacherAnalytics()
    fun onSettingsClick() {

    }
}



