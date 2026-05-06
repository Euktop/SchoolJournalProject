package stud.euktop.data.mock

import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.School

object MockRoomDataSource {
    private val _rooms = mutableListOf<Room>().apply {
        val school1 = MockSchoolDataSource.getAll().firstOrNull() ?: School(1, "Школа №1", "Р-н", "Адрес")
        add(Room(1, school1, "101"))
        add(Room(2, school1, "102"))
        add(Room(3, school1, "103"))
    }

    fun getAll(): List<Room> = _rooms.toList()
    fun get(roomId: Int): Room? = _rooms.find { it.roomId == roomId }
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