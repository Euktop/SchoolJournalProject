// data/src/main/java/stud/euktop/data/mock/repository/RoomAdminMockRepositoryImpl.kt
package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockRoomDataSource
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.RoomFilter
import stud.euktop.domain.model.school.RoomUpdate
import stud.euktop.domain.repository.RoomAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomAdminMockRepositoryImpl @Inject constructor() : RoomAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getRooms(filter: RoomFilter): Result<List<Room>> {
        logger?.i(tag, "getRooms started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val all = MockRoomDataSource.getAll()
            val filtered = all.filter { room ->
                (filter.schoolId == null || room.schoolId == filter.schoolId) &&
                        (filter.name == null || room.name.contains(
                            filter.name!!,
                            ignoreCase = true
                        ))
            }
            val paged = filtered.drop(filter.pagination.offset ?: 0)
                .take(filter.pagination.limit ?: Int.MAX_VALUE)
            Result.success(paged)
        } catch (e: Exception) {
            logger?.e(tag, "getRooms failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getRoom(roomId: Int): Result<Room> {
        logger?.i(tag, "getRoom started", "roomId=$roomId")
        MockDelayService.delay()
        return try {
            val room = MockRoomDataSource.get(roomId)
            if (room != null) {
                logger?.i(tag, "getRoom succeeded", "roomId=$roomId")
                Result.success(room)
            } else {
                val ex = NoSuchElementException("Room not found")
                logger?.e(tag, "getRoom failed", ex, "roomId=$roomId not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "getRoom exception", e, "roomId=$roomId")
            Result.failure(e)
        }
    }

    override suspend fun addRoom(room: Room): Result<Room> {
        logger?.i(tag, "addRoom started", "room=$room")
        MockDelayService.delay()
        return try {
            val newRoom = MockRoomDataSource.add(room)
            logger?.i(tag, "addRoom succeeded", "newId=${newRoom.roomId}")
            Result.success(newRoom)
        } catch (e: Exception) {
            logger?.e(tag, "addRoom failed", e, "room=$room")
            Result.failure(e)
        }
    }

    override suspend fun updateRoom(room: RoomUpdate): Result<Room> {
        logger?.i(tag, "updateRoom started", "roomUpdate=$room")
        MockDelayService.delay()
        return try {
            val existing = MockRoomDataSource.get(room.roomId)
                ?: return Result.failure(NoSuchElementException("Room not found"))
            val updatedSchoolId = room.schoolId.uValue ?: existing.schoolId
            val updatedName = room.name.uValue ?: existing.name
            val updatedRoom = existing.copy(schoolId = updatedSchoolId, name = updatedName)
            MockRoomDataSource.update(updatedRoom)
            Result.success(updatedRoom)
        } catch (e: Exception) {
            logger?.e(tag, "updateRoom failed", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteRoom(roomId: Int): Result<Unit> {
        logger?.i(tag, "deleteRoom started", "roomId=$roomId")
        MockDelayService.delay()
        return try {
            val deleted = MockRoomDataSource.delete(roomId)
            if (deleted) {
                logger?.i(tag, "deleteRoom succeeded", "roomId=$roomId")
                Result.success(Unit)
            } else {
                val ex = NoSuchElementException("Room not found")
                logger?.e(tag, "deleteRoom failed", ex, "roomId=$roomId not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "deleteRoom exception", e, "roomId=$roomId")
            Result.failure(e)
        }
    }
}