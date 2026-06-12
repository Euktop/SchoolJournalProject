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
        router.toAdminDashboard()
    }

    fun onSchoolsClick() {
        router.toAdminSchoolsList()
    }

    fun onClassesClick() {
        router.toAdminClassesList()
    }

    fun onSubjectsClick() {
        router.toAdminSubjectsList()
    }

    fun onRoomsClick() {
        router.toAdminRoomsList()
    }

    fun onAssignmentsClick() {
        router.toAdminAssignmentsList()
    }

    fun onUsersClick() {
        router.toAdminUsersList()
    }

    fun onAuditClick() {
        router.toAdminAuditLog()
    }

    fun onSettingsClick() {
        router.toAdminSettings()
    }
}