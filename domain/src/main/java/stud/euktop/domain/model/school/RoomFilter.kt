package stud.euktop.domain.model.school

/**
 * Учебный кабинет.
 * Принадлежит конкретной школе.
 */
data class RoomFilter(
    val school: Int? = null,
    val name: String? = null
) {
/*    companion object {
        fun exec(room: Room?) =
            RoomFilter(
                school = SchoolFilter.exec(room?.school),
                name = room?.name
            )
    }*/
}