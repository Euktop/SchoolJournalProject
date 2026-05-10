package stud.euktop.domain.model.assignment

import java.util.Date

data class AssignmentId(
    val teacherId: Int,
    val classId: Int,
    val subjectId: Int,
    val validFrom: Date
)