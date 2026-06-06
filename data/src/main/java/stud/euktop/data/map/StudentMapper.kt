package stud.euktop.data.map

import com.schooljournal.model.GetStudentGradesSummaryResult
import com.schooljournal.model.GetStudentMarksAggregatedResult
import com.schooljournal.model.GetStudentOverallAverageResult
import stud.euktop.domain.model.student.StudentGradesSummary
import stud.euktop.domain.model.student.StudentMarksAggregated
import stud.euktop.domain.model.student.StudentOverallAverage

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