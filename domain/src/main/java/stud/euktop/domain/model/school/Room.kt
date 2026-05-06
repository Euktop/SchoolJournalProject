package stud.euktop.domain.model.school

import stud.euktop.domain.model.common.BaseModel

/**
 * Учебный кабинет.
 * Принадлежит конкретной школе.
 */
data class Room(
    val roomId: Int = 0,
    val school: School,
    val name: String
) : BaseModel {
    override val idKey: Int
        get() = roomId

}