package stud.euktop.schooljournal.presentation.main.admin.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.coordinator.AdminCoordinator
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import javax.inject.Inject

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val coordinator: AdminCoordinator,
    private val router: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<AdminHomeState, Unit>() {

    override fun initState() = AdminHomeState()

    init {
        executeCoordinator = coordinatorExec
        loadAdminProfile()
    }

    private fun loadAdminProfile() {
        executeCoordinatorResultLoadingBlockSync(
            key = "load_profile", block = { coordinator.getCurrentUser() }) { user ->
            _state.update {
                it.copy(
                    adminName = "${user.firstName} ${user.lastName}".trim(), adminEmail = user.email
                )
            }
        }
    }

    // методы onDashboardClick, onSchoolsClick и т.д. остаются без изменений
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