package stud.euktop.schooljournal.presentation.main.profile

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.contract.RoleRepository
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterMainMenu
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterProfile
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec,
    private val authRepository: AuthRepository,
    private val schoolAdminRepository: SchoolAdminRepository,
    private val roleRepository: RoleRepository,
    private val routerProfile: RouterProfile,
    private val routerMainMenu: RouterMainMenu
) : BaseViewModel<ProfileState, Unit>() {

    override fun initState() = ProfileState()

    init {
        executeCoordinator = coordinatorExec
        onResume()
    }

    fun onResume() {
        loadCurrentUser()
        loadSchoolName()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            withLoading("load_profile") {
                val user = executeResult { authRepository.getCurrentUser() }
                val role = executeResult { Result.success(roleRepository.getCurrentRole()) }
                _state.update {
                    it.copy(user = user.await(), role = role.await())
                }
            }
        }
    }

    private fun loadSchoolName() {
        executeWithResultLoadingSync(
            key = "load_school",
            block = {
                val schoolId = roleRepository.getCurrentSchoolId()
                if (schoolId != null) {
                    schoolAdminRepository.getSchool(schoolId)
                } else {
                    Result.success(null)
                }
            },
            onSuccess = { school ->
                _state.update { it.copy(school = school) }
            }
        )
    }

    fun onRoleClick() {
        viewModelScope.launch {
            routerMainMenu.toMainMenuSelectRole()
        }
    }

    fun logout() {
        executeWithResultLoadingSync(
            "logout",
            { authRepository.logout() },
            { routerProfile.toLogout() })
    }

    fun buttonProfileClick() {
        _state.value.user?.userId?.let {
            routerProfile.toEditUser(it)
        }
    }

    fun changePasswordClick() {
        routerProfile.toChangePassword()
    }
}