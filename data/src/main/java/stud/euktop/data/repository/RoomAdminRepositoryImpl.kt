package stud.euktop.data.repository

import com.schooljournal.api.RoomsApi
import com.schooljournal.model.CreateRoomRequest
import com.schooljournal.model.UpdateRoomRequest
import stud.euktop.data.map.toDomain
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.RoomFilter
import stud.euktop.domain.model.school.RoomUpdate
import stud.euktop.domain.repository.RoomAdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomAdminRepositoryImpl @Inject constructor(
    private val roomsApi: RoomsApi,
    private val errorHandler: ApiErrorHandler
) : RoomAdminRepository {

    override suspend fun getRooms(filter: RoomFilter): Result<List<Room>> =
        errorHandler.safeApiCall {
            roomsApi.apiRoomsFilterGet(
                schoolId = filter.schoolId,
                name = filter.name,
                offset = filter.pagination.offset,
                limit = filter.pagination.limit
            ).map { it.toDomain() }
        }

    override suspend fun getRoom(roomId: Int): Result<Room> =
        errorHandler.safeApiCall {
            roomsApi.apiRoomsIdGet(roomId).toDomain()
        }

    override suspend fun addRoom(room: Room): Result<Room> =
        errorHandler.safeApiCall {
            val request = CreateRoomRequest(
                schoolId = room.schoolId,
                name = room.name
            )
            roomsApi.apiRoomsPost(request).toDomain()
        }

    override suspend fun updateRoom(room: RoomUpdate): Result<Room> =
        errorHandler.safeApiCall {
            roomsApi.apiRoomsIdPut(
                id = room.roomId,
                updateRoomRequest = UpdateRoomRequest(
                    schoolId = room.schoolId.uValue,
                    name = room.name.uValue
                )
            ).toDomain()
        }

    override suspend fun deleteRoom(roomId: Int): Result<Unit> =
        errorHandler.safeApiCall {
            roomsApi.apiRoomsIdDelete(roomId)
        }
}