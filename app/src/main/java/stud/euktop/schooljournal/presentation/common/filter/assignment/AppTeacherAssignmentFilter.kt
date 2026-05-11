package stud.euktop.schooljournal.presentation.common.filter.assignment

import stud.euktop.domain.model.assignment.TeacherAssignmentFilter
import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserProfile
import java.util.Date

data class AppTeacherAssignmentFilter(
    val teacher: UserProfile? = null,
    val classInfo: ClassInfo? = null,
    val subject: Subject? = null,
    val isPrimary: Boolean? = null,
    val validFrom: Date? = null,
    val validTo: Date? = null,
    val pagination: Pagination = Pagination()
) {
    fun toDomain(): TeacherAssignmentFilter = TeacherAssignmentFilter(
        teacherId = teacher?.userId,
        classId = classInfo?.classId,
        subjectId = subject?.subjectId,
        isPrimary = isPrimary,
        validFrom = validFrom,
        validTo = validTo,
        pagination = pagination
    )
}

fun TeacherAssignmentFilter.toApp(
    teacher: UserProfile?,
    classInfo: ClassInfo?,
    subject: Subject?,
) = AppTeacherAssignmentFilter(
    teacher = teacher,
    classInfo = classInfo,
    subject = subject,
    isPrimary = isPrimary,
    validFrom = validFrom,
    validTo = validTo,
    pagination = pagination
)