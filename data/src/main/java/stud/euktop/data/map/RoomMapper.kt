package stud.euktop.data.map

import com.schooljournal.model.CreateRoomResult
import com.schooljournal.model.GetRoomByIdResult
import com.schooljournal.model.GetRoomsResult
import com.schooljournal.model.UpdateRoomResult
import stud.euktop.domain.model.school.Room

private fun toRoom(roomId: Int?, schoolId: Int?, name: String?) = Room(
    roomId = roomId ?: 0,
    schoolId = schoolId ?: 0,
    name = name ?: ""
)

internal fun GetRoomsResult.toDomain(): Room = toRoom(roomId, schoolId, name)

internal fun GetRoomByIdResult.toDomain(): Room = toRoom(roomId, schoolId, name)

internal fun CreateRoomResult.toDomain(): Room = toRoom(roomId, schoolId, name)

internal fun UpdateRoomResult.toDomain(): Room = toRoom(roomId, schoolId, name)