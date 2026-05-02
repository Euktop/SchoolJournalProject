package stud.euktop.domain.model.school

import stud.euktop.domain.model.common.PrimaryKey

/**
 * Школа (учебное заведение).
 */
data class School(
    val schoolId: Int = 0,
    val name: String = "",
    val region: String = "",
    val address: String = ""
) : PrimaryKey<Int> {
    override val idKey: Int = schoolId
}