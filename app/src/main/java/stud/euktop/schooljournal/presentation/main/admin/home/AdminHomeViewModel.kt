package stud.euktop.schooljournal.presentation.main.admin.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import javax.inject.Inject

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val router: RouterAdmin
) : ViewModel() {

    private val _state = MutableStateFlow(AdminHomeState())
    val state: StateFlow<AdminHomeState> = _state.asStateFlow()

    init {
        loadAdminProfile()
    }

    private fun loadAdminProfile() {

    }

    fun onDashboardClick() {
        router.toDashboard()
    }

    fun onSchoolsClick() {
        router.toSchoolsList()
    }

    fun onClassesClick() {
        router.toClassesList()
    }

    fun onSubjectsClick() {
        router.toSubjectsList()
    }

    fun onRoomsClick() {
        router.toRoomsList()
    }

    fun onAssignmentsClick() {
        router.toAssignmentsList()
    }

    fun onUsersClick() {
        router.toUsersList()
    }

    fun onAuditClick() {
        router.toAuditLog()
    }

    fun onSettingsClick() {
        router.toSettings()
    }
}