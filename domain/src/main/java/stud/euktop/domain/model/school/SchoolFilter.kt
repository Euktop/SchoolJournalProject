package stud.euktop.domain.model.school

import stud.euktop.domain.model.common.Pagination

/**
 * Школа (учебное заведение).
 */
data class SchoolFilter(
    val name: String? = null,
    val region: String? = null,
    val address: String? = null,
    val pagination: Pagination = Pagination()
)