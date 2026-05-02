package stud.euktop.domain.model.school

import stud.euktop.domain.model.user.UserInfo

/**
 * Информация о классе (параллель, буква, учебный год, школа, классный руководитель).
 */
data class ClassInfo(
    val classId: Int = 0,
    val school: School = School(),
    val grade: Int = 0,
    val letter: String = "",
    val academicYearStart: Int = -1,
    val academicYearEnd: Int = -1,
    val teacher: UserInfo? = null,
) {
    val name
        get() = "$grade $letter ${school.name}"
}