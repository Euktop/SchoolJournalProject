package stud.euktop.domain.model.school

import stud.euktop.domain.model.common.PrimaryKey

/**
 * Учебный предмет (например, Математика, Физика).
 */
data class Subject(
    val subjectId: Int = 0,
    val name: String = "",
    val description: String? = null
) : PrimaryKey<Int> {
    override val idKey: Int
        get() = subjectId
}