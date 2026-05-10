package stud.euktop.domain.model.assignment

import stud.euktop.domain.model.common.Pagination
import java.util.Date

data class TeacherAssignmentFilter(
    val teacherId: Int? = null,
    val classId: Int? = null,
    val subjectId: Int? = null,
    val isPrimary: Boolean? = null,
    val validFrom: Date? = null,
    val validTo: Date? = null,
    val filterByValidTo: Boolean? = null,
    val pagination: Pagination = Pagination()
)