package stud.euktop.domain.model.assignment

import java.util.Date

data class TeacherAssignmentFilter(
    val teacher: Int? = null,
    val classInfo: Int? = null,
    val subject: Int? = null,
    val validFromDate: Date? = null,
    val isPrimary: Boolean? = null
) {
/*    companion object {
        fun exec(teacherAssignment: TeacherAssignment?) =if (teacherAssignment==null)null else
            TeacherAssignmentFilter(
                teacher = UserInfoFilter.exec(teacherAssignment.teacher),
                classInfo = ClassInfoFilter.exec(teacherAssignment.classInfo),
                subject = SubjectFilter.exec(teacherAssignment.subject),
                validFromDate = teacherAssignment.validFromDate,
                isPrimary = teacherAssignment.isPrimary
            )
    }*/
}