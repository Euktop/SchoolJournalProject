package stud.euktop.domain.model.assignment

import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Subject
import java.util.Date

/**
 * Назначение учителя на класс и предмет на определённый период.
 * Поле isPrimary указывает, является ли этот учитель основным по данному предмету в классе.
 */
data class TeacherAssignment(
    val id: Int = 0,
    val teacher: UserInfo,
    val classInfo: ClassInfo,
    val subject: Subject,
    val validFromDate: Date,
    val validToDate: Date? = null,
    val isPrimary: Boolean = false
)