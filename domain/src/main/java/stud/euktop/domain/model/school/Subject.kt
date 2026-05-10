package stud.euktop.domain.model.school

/**
 * Учебный предмет (например, Математика, Физика).
 */
data class Subject(
    val subjectId: Int = 0,
    val name: String,
    val description: String?
) {
    companion object {
        fun createObject(
            subjectId: Int?,
            name: String?,
            description: String?
        ) = Subject(
            subjectId = subjectId ?: 0,
            name = name ?: "",
            description = description
        )
    }
}