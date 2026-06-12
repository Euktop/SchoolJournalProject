package stud.euktop.schooljournal.presentation.main.student.schedule

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.filter.lesson.AppLessonFilter
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class StudentScheduleViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val authRepository: AuthRepository,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<StudentScheduleState, Unit>() {

    private var studentId: Int? = null

    override fun initState() = StudentScheduleState()

    init {
        executeCoordinator = coordinatorExec
        loadCurrentStudent()
    }

    private fun loadCurrentStudent() {
        viewModelScope.launch {
            executeWithResultLoading(
                key = "load_student",
                block = { authRepository.getCurrentUser() },
                onSuccess = { userInfo ->
                    studentId = userInfo.userId
                    loadSchedule()
                }
            )
            withLoading("load_student") {
                val user = executeCoordinatorResult {
                    authRepository.getCurrentUser()
                }
                val class1 = executeCoordinatorResult {
                    studentRepository.getStudentClass()
                }
                user.await()?.let {
                    studentId = it.userId
                    loadSchedule()
                }
                class1.await().let { classInfo ->
                    _state.update {
                        it.copy(
                            filter = it.filter.copy(
                                classInfo = classInfo,
                                dateFrom = Date(),
                                dateTo = Date()
                            )
                        )
                    }
                }
            }
        }
    }

    fun loadSchedule() {
        val currentFilter = _state.value.filter
        executeWithResultLoadingSync(
            key = "load_schedule",
            block = {
                studentRepository.getStudentSchedule(
                    studentId = studentId,
                    startDate = currentFilter.dateFrom,
                    endDate = currentFilter.dateTo
                )
            }
        ) { items ->
            _state.update { it.copy(schedule = items) }
        }
    }

    fun applyFilter(filter: AppLessonFilter) {
        _state.update { it.copy(filter = filter, schedule = emptyList()) }
        loadSchedule()
    }
}