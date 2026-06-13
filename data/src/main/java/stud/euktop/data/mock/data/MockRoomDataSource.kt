// data/src/main/java/stud/euktop/data/mock/data/MockRoomDataSource.kt
package stud.euktop.data.mock.data

import stud.euktop.domain.model.school.Room

internal object MockRoomDataSource {
    private val _rooms = mutableListOf<Room>().apply {
        val school1Id = 1
        add(Room(1, school1Id, "101"))
        add(Room(2, school1Id, "102"))
        add(Room(3, school1Id, "103"))
    }

    fun getAll(): List<Room> = _rooms.toList()
    fun get(roomId: Int): Room? = _rooms.find { it.roomId == roomId }
        ?: _rooms.firstOrNull() ?: Room(roomId = roomId, schoolId = 0, name = "Неизвестный")
    fun add(room: Room): Room {
        val newId = (_rooms.maxOfOrNull { it.roomId } ?: 0) + 1
        val newRoom = room.copy(roomId = newId)
        _rooms.add(newRoom)
        return newRoom
    }
    fun update(room: Room) {
        val index = _rooms.indexOfFirst { it.roomId == room.roomId }
        if (index >= 0) _rooms[index] = room
    }
    fun delete(roomId: Int): Boolean = _rooms.removeIf { it.roomId == roomId }
}