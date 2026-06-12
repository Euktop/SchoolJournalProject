package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.model.schedule.StudentScheduleItem
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class StudentSubjectDetailState(
    val subjectSummary: StudentSubjectSummary? = null,
    val scheduleItems: List<StudentScheduleItem> = emptyList(),
    val overallAverage: Double? = null,
    val trendFormatted: String = "",
    val marksPagingDataFlow: Flow<PagingData<StudentSubjectMark>>? = null,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<StudentSubjectDetailState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}