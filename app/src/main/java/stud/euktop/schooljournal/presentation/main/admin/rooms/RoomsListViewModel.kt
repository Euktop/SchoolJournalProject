package stud.euktop.schooljournal.presentation.main.admin.rooms

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.RoomFilter
import stud.euktop.domain.repository.RoomAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import javax.inject.Inject

@HiltViewModel
class RoomsListViewModel @Inject constructor(
    private val roomRepository: RoomAdminRepository,
    private val routerAdmin: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<RoomsListState, Unit>() {
    init {
        executeCoordinator = coordinatorExec
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

    override fun initState() = RoomsListState()
    private var currentOffset = 0
    var hasMore = true

    fun loadNextPage() {
        if (isLoading("pagination") || !hasMore) return
        val offset = currentOffset
        executeWithResultLoadingSync(
            key = "pagination",
            block = {
                roomRepository.getRooms(
                    _state.value.filter.copy(pagination = Pagination(offset, PAGE_SIZE))
                )
            }
        ) { newRooms ->
            val isFirstPage = offset == 0
            _state.update { it.copy(rooms = if (isFirstPage) newRooms else it.rooms + newRooms) }
            currentOffset += newRooms.size
            hasMore = newRooms.size == PAGE_SIZE
        }
    }

    fun applyFilter(filter: RoomFilter) {
        _state.update { it.copy(filter = filter, rooms = emptyList()) }
        currentOffset = 0
        hasMore = true
        loadNextPage()
    }

    fun deleteRoom(roomId: Int) {
        executeWithResultLoadingSync("delete", { roomRepository.deleteRoom(roomId) }) { refresh() }
    }

    private fun refresh() {
        currentOffset = 0
        hasMore = true
        _state.update { it.copy(rooms = emptyList()) }
        loadNextPage()
    }

    fun editRoom(room: Room) {

    }
}

