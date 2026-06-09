package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import java.util.Date

data class GradeFilter(
    val startDate: Date? = null,
    val endDate: Date? = null,
    val maxPoints: Int = 30
)