// presentation/main/student/studentSubjectDetail/StudentSubjectDetailViewModel.kt
package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.paging.StudentMarksPagingSource
import javax.inject.Inject

@HiltViewModel
class StudentSubjectDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val studentRepository: StudentRepository,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<StudentSubjectDetailState, Unit>() {

    private val subjectId = savedStateHandle.get<Int>("subjectId") ?: 0
    private val currentStudentId: Int? = savedStateHandle.get<Int>("studentId")

    private val _filterFlow = MutableStateFlow(GradeFilter())
    val filterFlow: StateFlow<GradeFilter> = _filterFlow.asStateFlow()

    val pagingDataFlow: Flow<PagingData<StudentSubjectMark>> = _filterFlow.flatMapLatest { filter ->
        Pager(PagingConfig(pageSize = 20)) {
            StudentMarksPagingSource(
                repository = studentRepository,
                subjectId = subjectId,
                studentId = currentStudentId,
                startDate = filter.startDate,
                endDate = filter.endDate
            )
        }.flow.cachedIn(viewModelScope)
    }

    override fun initState() = StudentSubjectDetailState()

    init {
        executeCoordinator = coordinatorExec
        loadAggregatedMarks()
    }

    fun applyFilter(filter: GradeFilter) {
        _filterFlow.value = filter
        _state.update { it.copy(filter = filter) }
        loadAggregatedMarks()
    }

    private fun loadAggregatedMarks() {
        val filter = _filterFlow.value
        executeWithLoadingSync(
            key = "load_aggregated",
            block = {
                studentRepository.getMarksAggregated(
                    subjectId = subjectId,
                    studentId = currentStudentId,
                    startDate = filter.startDate,
                    endDate = filter.endDate,
                    maxPoints = filter.maxPoints
                )
            },
            onSuccess = { aggregated ->
                _state.update { it.copy(aggregatedMarks = aggregated) }
            }
        )
    }
}