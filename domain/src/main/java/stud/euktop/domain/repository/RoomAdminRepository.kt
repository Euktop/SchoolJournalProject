package stud.euktop.domain.repository

import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.RoomFilter
import stud.euktop.domain.model.school.RoomUpdate

interface RoomAdminRepository {
    suspend fun getRooms(filter: RoomFilter = RoomFilter()): Result<List<Room>>
    suspend fun getRoom(roomId: Int): Result<Room>
    suspend fun addRoom(room: Room): Result<Room>
    suspend fun updateRoom(room: RoomUpdate): Result<Room>
    suspend fun deleteRoom(roomId: Int): Result<Unit>
}