package stud.euktop.schooljournal.presentation.main.admin.dashboard

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.coordinator.AdminCoordinator
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterProfile
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val coordinator: AdminCoordinator,
    private val routerProfile: RouterProfile
) : BaseViewModel<AdminDashboardState, Unit>() {

    override fun initState() = AdminDashboardState()

    init {
        loadData()
    }

    private fun loadData() {
        // Зачем нам executeWithLoadingSync если нам даётся уже готовый CoordinatorResult
        // Также нам не нужен coordinatorExec - т.к. координатор сам уже делает
        executeLoadingBlockSync("load_admin", { coordinator.getDashboardStatistics() }) { stats ->
            _state.update {
                it.copy(
                    schoolsCount = stats.schoolsCount,
                    activeUsersCount = stats.activeUsersCount,
                    totalStudents = stats.totalStudents,
                    totalTeachers = stats.totalTeachers,
                    healthPercent = stats.healthPercent
                )
            }
        }
    }

    fun onProfileClick() {
        viewModelScope.launch { routerProfile.toProfile() }
    }
}