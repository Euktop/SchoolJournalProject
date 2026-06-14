package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.coordinator.StudentCoordinator
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterBack
import stud.euktop.schooljournal.presentation.common.paging.StudentMarksPagingSource
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class StudentSubjectDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val studentCoordinator: StudentCoordinator,
    private val studentRepository: StudentRepository,
    private val authRepository: AuthRepository,
    private val routerBack: RouterBack,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<StudentSubjectDetailState, Unit>() {

    private val subjectId: Int = savedStateHandle[KEY_SUBJECT_ID] ?: 0
    private var studentId: Int = 0

    override fun initState() = StudentSubjectDetailState()

    init {
        executeCoordinator = coordinatorExec
        loadStudentId()
    }

    private fun loadTrend() {
        executeCoordinatorResultLoadingBlockSync(
            key = "trend",
            block = { studentCoordinator.getSubjectTrend(subjectId, studentId) }
        ) { trend ->
            _state.update { it.copy(trendFormatted = trend.formattedString) }
        }
    }

    private fun loadStudentId() {
        executeWithResultLoadingSync(
            key = "load_student",
            block = { authRepository.getCurrentUser() }
        ) { user ->
            studentId = user.userId
            loadSubjectSummary()
            loadSchedule()
            loadTrend()
            setupMarksPaging()
            loadGraphic()
        }
    }

    private fun loadSubjectSummary() {
        executeCoordinatorResultLoadingBlockSync(
            key = "load_subject",
            block = { studentCoordinator.getSubjectsSummary(studentId) }
        ) { summaries ->
            val summary = summaries.find { it.subjectId == subjectId }
            summary?.let {
                _state.update { state ->
                    state.copy(
                        subjectSummary = it,
                        overallAverage = it.averageMark
                    )
                }
            }
        }
    }

    private fun loadGraphic() {
        executeCoordinatorResultLoadingBlockSync(
            key = "load_graphic",
            block = {
                val oneMonthAgoDate: Date = LocalDate.now()
                    .minusMonths(1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .let { Date.from(it) }
                studentCoordinator.getMarksAggregated(
                    subjectId = subjectId,
                    studentId = studentId,
                    startDate = Date(),
                    endDate = oneMonthAgoDate,
                    maxPoints = 50
                )
            }
        ) {

        }
    }

    private fun loadSchedule() {
        executeWithResultLoadingSync(
            key = "load_schedule",
            block = { studentRepository.getStudentSchedule(studentId) }
        ) { schedule ->
            _state.update { it.copy(scheduleItems = schedule) }
        }
    }

    private fun setupMarksPaging() {
        val pagingFlow = Pager(PagingConfig(pageSize = 20)) {
            StudentMarksPagingSource(studentRepository, subjectId, studentId, null, null)
        }.flow.cachedIn(viewModelScope)

        _state.update { it.copy(marksPagingDataFlow = pagingFlow) }
    }

    fun onBackClick() {
        viewModelScope.launch { routerBack.toBack() }
    }

    companion object {
        const val KEY_SUBJECT_ID = "subjectId"
    }
}