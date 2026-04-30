package stud.euktop.domain.model.school
/**
 * Учебный предмет (например, Математика, Физика).
 */
data class Subject(
    val subjectId: Int,
    val name: String,
    val description: String?
)