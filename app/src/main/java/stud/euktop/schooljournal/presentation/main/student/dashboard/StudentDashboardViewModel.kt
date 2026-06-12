package stud.euktop.schooljournal.presentation.main.student.dashboard

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.domain.repository.HomeworkRepository
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterStudent
import javax.inject.Inject

@HiltViewModel
class StudentDashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val studentRepository: StudentRepository,
    private val homeworkRepository: HomeworkRepository,
    private val routerStudent: RouterStudent,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<StudentDashboardState, Unit>() {

    override fun initState() = StudentDashboardState()

    init {
        executeCoordinator = coordinatorExec
        loadData()
    }

    private fun loadData() {
        loadProfile()
        loadSchedule()
        loadHomeworks()
        loadSubjects()
        loadOverallAverage()
    }

    private fun loadProfile() {
        executeWithResultLoadingSync(
            key = "profile",
            block = { authRepository.getCurrentUser() },
            onSuccess = { user ->
                val name = "${user.firstName} ${user.lastName}".trim()
                _state.update { it.copy(studentName = name.ifEmpty { "Ученик" }) }
            }
        )
    }

    private fun loadSchedule() {
        val todayStart = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0); set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0); set(java.util.Calendar.MILLISECOND, 0)
        }.time
        val todayEnd = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 23); set(java.util.Calendar.MINUTE, 59)
            set(java.util.Calendar.SECOND, 59)
        }.time

        executeWithResultLoadingSync(
            key = "schedule",
            block = {
                studentRepository.getStudentSchedule(
                    startDate = todayStart,
                    endDate = todayEnd
                )
            },
            onSuccess = { schedule ->
                val now = java.util.Date()
                val nextLesson = schedule.firstOrNull { it.date.after(now) }

                _state.update {
                    it.copy(
                        lessonsTodayCount = schedule.size,
                        isNextLessonVisible = nextLesson != null,
                        nextLessonName = nextLesson?.subjectName ?: "",
                        // Сырые данные для форматирования во Fragment
                        nextLessonDetails = "${nextLesson?.roomName.orEmpty()}|${nextLesson?.teacherLastName.orEmpty()}",
                        nextLessonTime = "СКОРО"
                    )
                }
            }
        )
    }

    private fun loadHomeworks() {
        executeWithResultLoadingSync(
            key = "homeworks",
            block = { homeworkRepository.getHomeworks(stud.euktop.domain.model.homework.HomeworkFilter()) },
            onSuccess = { homeworks ->
                val notSubmitted = homeworks.count { !it.isSubmitted }
                _state.update {
                    it.copy(
                        newHomeworksCount = notSubmitted,
                        homeworksNotSubmitted = notSubmitted
                    )
                }
            }
        )
    }

    private fun loadSubjects() {
        executeWithResultLoadingSync(
            key = "subjects",
            block = { studentRepository.getSubjectsSummary() },
            onSuccess = { subjects ->
                _state.update { it.copy(subjectsCount = subjects.size) }
            }
        )
    }

    private fun loadOverallAverage() {
        executeWithResultLoadingSync(
            key = "average",
            block = { studentRepository.getOverallAverage() },
            onSuccess = { overall ->
                _state.update { it.copy(averageMark = overall.averageMark) }
            }
        )
    }

    // Навигация (вызывается из Fragment)
    fun onScheduleClick() = routerStudent.toStudentSchedule()
    fun onSubjectsClick() = routerStudent.toStudentSubjects()
    fun onHomeworkClick() = routerStudent.toStudentHomework()
    fun onProfileClick() = routerStudent.toStudentProfile()
}