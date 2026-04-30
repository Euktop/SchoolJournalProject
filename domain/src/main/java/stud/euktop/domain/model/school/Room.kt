package stud.euktop.domain.model.school
/**
 * Учебный кабинет.
 * Принадлежит конкретной школе.
 */
data class Room(
    val roomId: Int = 0,
    val school: School,
    val name: String
)