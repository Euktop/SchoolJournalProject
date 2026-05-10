package stud.euktop.domain.model.school

import stud.euktop.domain.model.common.Pagination

/**
 * Учебный кабинет.
 * Принадлежит конкретной школе.
 */
data class RoomFilter(
    val schoolId: Int? = null,
    val name: String? = null,
    val pagination: Pagination = Pagination()
)