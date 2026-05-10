package stud.euktop.domain.model.assignment

import java.util.Date

data class TeacherAssignment(
    val assignmentId: AssignmentId,
    val validToDate: Date?,
    val isPrimary: Boolean = false
)