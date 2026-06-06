package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import stud.euktop.domain.model.student.StudentMarksAggregated
import stud.euktop.schooljournal.presentation.common.base.BaseState
import java.util.Date

data class GradeFilter(
    val startDate: Date? = null,
    val endDate: Date? = null,
    val maxPoints: Int = 30
)

data class StudentSubjectDetailState(
    val aggregatedMarks: List<StudentMarksAggregated> = emptyList(),
    val filter: GradeFilter = GradeFilter(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<StudentSubjectDetailState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}