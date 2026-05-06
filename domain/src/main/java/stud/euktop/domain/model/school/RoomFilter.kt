package stud.euktop.domain.model.school

/**
 * Учебный кабинет.
 * Принадлежит конкретной школе.
 */
data class RoomFilter(
    val school: School? = null,
    val schoolFilter: SchoolFilter = SchoolFilter(),
    val name: String? = null
)