package stud.euktop.domain.model.school

import stud.euktop.domain.model.common.Field

/**
 * Школа (учебное заведение).
 */
data class SchoolUpdate(
    val schoolId: Int = 0,
    val name: Field<String> = Field(),
    val region: Field<String> = Field(),
    val address: Field<String> = Field()
)