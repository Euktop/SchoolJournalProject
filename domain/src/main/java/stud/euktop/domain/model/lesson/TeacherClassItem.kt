package stud.euktop.domain.model.lesson

/**
 * Упрощённая модель для списка классов, которые ведёт учитель.
 * Содержит только необходимое для отображения.
 */
data class TeacherClassItem(
    val classId: Int,
    val schoolName: String?,
    val grade: Int,
    val letter: String,
    val academicYearStart: Int,
    val academicYearEnd: Int,
    val subjectId: Int,
    val subjectName: String?
)