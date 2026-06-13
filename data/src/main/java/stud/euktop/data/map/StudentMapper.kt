package stud.euktop.data.map

import com.schooljournal.model.GetStudentGradesSummaryResult
import com.schooljournal.model.GetStudentMarkTrendResult
import com.schooljournal.model.GetStudentMarksAggregatedResult
import com.schooljournal.model.GetStudentOverallAverageResult
import stud.euktop.domain.model.student.StudentGradesSummary
import stud.euktop.domain.model.student.StudentMarksAggregated
import stud.euktop.domain.model.student.StudentOverallAverage
import stud.euktop.domain.model.student.SubjectTrend

fun GetStudentGradesSummaryResult.toDomain(): StudentGradesSummary = StudentGradesSummary(
    subjectId = subjectId ?: 0,
    subjectName = subjectName ?: "",
    averageMark = averageMark,
    finalMark = finalMark,
    totalGrades = totalGrades,
    goodGrades = goodGrades,
    excellentGrades = excellentGrades
)

fun GetStudentMarksAggregatedResult.toDomain(): StudentMarksAggregated = StudentMarksAggregated(
    date = periodStart?.toDate() ?: java.util.Date(),
    averageMark = averageMark,
    marksCount = gradesCount
)

fun GetStudentOverallAverageResult.toDomain(): StudentOverallAverage = StudentOverallAverage(
    averageMark = totalAverage,
    totalGrades = totalGrades,
    goodGrades = goodGrades,
    excellentGrades = excellentGrades
)

internal fun GetStudentMarkTrendResult.toDomain(): SubjectTrend {
    return SubjectTrend(
        value = this.trendValue ?: 0.0,
        isPositive = this.isPositive ?: false,
        formattedString = this.formattedString ?: ""
    )
}