// RoomAdminRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockRoomDataSource
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.RoomFilter
import stud.euktop.domain.repository.RoomAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomAdminRepositoryImpl @Inject constructor() : RoomAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getRooms(filter: RoomFilter): Result<List<Room>> {
        logger?.i(tag, "getRooms started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val rooms = MockRoomDataSource.getAll()
            logger?.i(tag, "getRooms succeeded", "count=${rooms.size}")
            Result.success(rooms)
        } catch (e: Exception) {
            logger?.e(tag, "getRooms failed", e, "filter=$filter")
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

    override suspend fun updateRoom(room: Room): Result<Room> {
        logger?.i(tag, "updateRoom started", "room=$room")
        MockDelayService.delay()
        return try {
            MockRoomDataSource.update(room)
            logger?.i(tag, "updateRoom succeeded", "roomId=${room.roomId}")
            Result.success(room)
        } catch (e: Exception) {
            logger?.e(tag, "updateRoom failed", e, "room=$room")
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