package stud.euktop.schooljournal.presentation.main.admin.rooms

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.model.common.Field
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.RoomUpdate
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.coordinator.AdminCoordinator
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import javax.inject.Inject

@HiltViewModel
class RoomEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val coordinator: AdminCoordinator,
    private val schoolRepository: SchoolAdminRepository,
    private val routerAdmin: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<RoomEditState, Unit>() {

    private val roomId: Int = savedStateHandle["roomId"] ?: 0
    private val isEditMode get() = roomId != 0

    override fun initState() = RoomEditState(roomId = roomId)

    init {
        executeCoordinator = coordinatorExec
        if (isEditMode) loadRoom()
    }

    private fun loadRoom() {
        executeCoordinatorResultLoadingBlockSync("load", { coordinator.getRoom(roomId) }) { room ->
            val school = schoolRepository.getSchool(room.schoolId).getOrNull()
            _state.update {
                it.copy(
                    school = school,
                    name = it.name.copy(room.name),
                    originalSchoolId = room.schoolId,
                    originalName = room.name
                )
            }
        }
    }

    fun updateSchool(school: School?) {
        _state.update { it.copy(school = school) }
    }

    fun updateName(name: String) {
        _state.update { it.copy(name = it.name.copy(name)) }
    }

    fun save() {
        val state = _state.value
        if (!state.isFormValid()) return
        if (isEditMode) {
            val update = RoomUpdate(
                roomId = roomId,
                schoolId = Field(
                    state.school?.schoolId,
                    state.school?.schoolId != state.originalSchoolId
                ),
                name = Field(state.name.getValidate(), state.name.value != state.originalName)
            )
            executeCoordinatorResultLoadingBlockSync(
                "save",
                { coordinator.updateRoom(update) }) { routerAdmin.toBack() }
        } else {
            val newRoom = Room(
                schoolId = state.school!!.schoolId,
                name = state.name.getValidate()
            )
            executeCoordinatorResultLoadingBlockSync(
                "save",
                { coordinator.addRoom(newRoom) }) { routerAdmin.toBack() }
        }
    }

    fun cancel() {
        viewModelScope.launch { routerAdmin.toBack() }
    }

    fun getSchoolsPagingDataFlow(filter: SchoolFilter): Flow<PagingData<School>> =
        coordinator.getSchoolsPagingDataFlow(filter)
}