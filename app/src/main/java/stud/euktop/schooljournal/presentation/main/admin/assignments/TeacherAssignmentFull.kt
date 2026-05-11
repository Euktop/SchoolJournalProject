package stud.euktop.schooljournal.presentation.main.admin.assignments

import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserProfile
import java.util.Date

data class TeacherAssignmentFull(
    val assignmentId: AssignmentId,
    val teacher: UserProfile?,
    val classInfo: ClassInfo?,
    val subject: Subject?,
    val validToDate: Date?,
    val isPrimary: Boolean
)