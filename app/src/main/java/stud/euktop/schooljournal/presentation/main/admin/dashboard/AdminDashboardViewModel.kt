package stud.euktop.schooljournal.presentation.main.admin.dashboard

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterProfile
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val routerProfile: RouterProfile,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<AdminDashboardState, Unit>() {

    override fun initState() = AdminDashboardState()

    init {
        executeCoordinator = coordinatorExec
        loadData()
    }

    private fun loadData() {
        executeWithLoadingSync("load_admin", { authRepository.getCurrentUser() }) { user ->
            _state.update { it.copy(adminName = "${user.firstName} ${user.lastName}".trim()) }
        }
        // Mock‑данные для статистики (замените на реальные вызовы репозиториев)
        _state.update {
            it.copy(
                schoolsCount = 12,
                activeUsersCount = 245,
                totalStudents = 3420,
                totalTeachers = 184,
                healthPercent = 98
            )
        }
    }

    fun onGenerateReport() { /* TODO */ }
    fun onInviteUsers() { /* TODO */ }
    fun onMaintenance() { /* TODO */ }

    fun onProfileClick() {
        viewModelScope.launch { routerProfile.toProfile() }
    }
}