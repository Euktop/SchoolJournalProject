package stud.euktop.domain.model.school

/**
 * Школа (учебное заведение).
 */
data class School(
    val schoolId: Int,
    val name: String,
    val region: String,
    val address: String
)