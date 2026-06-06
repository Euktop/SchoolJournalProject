package stud.euktop.domain.model.student

import java.util.Date

data class StudentMarksAggregated(
    val date: Date,
    val averageMark: Double?,
    val marksCount: Int?
)