package stud.euktop.domain.model.school

/**
 * Информация о классе (параллель, буква, учебный год, школа, классный руководитель).
 */
data class ClassInfo(
    val classId: Int = 0,
    val schoolId: Int,
    val grade: Int,
    val letter: String,
    val academicYearStart: Int,
    val academicYearEnd: Int,
    val teacherId: Int?
) {
    companion object {
        fun createObject(
            classId: Int?,
            schoolId: Int?,
            grade: Int?,
            letter: String?,
            academicYearStart: Int?,
            academicYearEnd: Int?,
            teacherId: Int?
        ) = ClassInfo(
            classId = classId ?: 0,
            schoolId = schoolId ?: 0,
            grade = grade ?: 0,
            letter = letter ?: "",
            academicYearStart = academicYearStart ?: 0,
            academicYearEnd = academicYearEnd ?: 0,
            teacherId = teacherId
        )
    }
}