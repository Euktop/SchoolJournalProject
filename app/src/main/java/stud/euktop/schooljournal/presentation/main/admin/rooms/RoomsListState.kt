package stud.euktop.schooljournal.presentation.main.admin.rooms

import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.RoomFilter
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class RoomsListState(
    val rooms: List<Room> = emptyList(),
    val filter: RoomFilter = RoomFilter(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<RoomsListState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}

