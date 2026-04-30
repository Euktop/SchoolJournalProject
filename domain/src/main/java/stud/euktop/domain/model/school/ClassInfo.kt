package stud.euktop.domain.model.school

import stud.euktop.domain.model.user.UserInfo

/**
 * Информация о классе (параллель, буква, учебный год, школа, классный руководитель).
 */
data class ClassInfo(
    val classId: Int,
    val school: School,
    val grade: Int,
    val letter: String,
    val academicYearStart: Int,
    val academicYearEnd: Int,
    val teacher: UserInfo? = null,
) {
    val name
        get() = "$grade $letter"
}