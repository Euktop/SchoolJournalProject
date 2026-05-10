package stud.euktop.domain.model.school

/**
 * Учебный кабинет.
 * Принадлежит конкретной школе.
 */
data class Room(
    val roomId: Int = 0,
    val schoolId: Int,
    val name: String
) {
    companion object {
        fun createObject(
            roomId: Int?,
            schoolId: Int?,
            name: String?
        ) = Room(
            roomId = roomId ?: 0,
            schoolId = schoolId ?: 0,
            name = name ?: ""
        )
    }
}