package stud.euktop.schooljournal.presentation.main.profile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.contract.RoleRepository
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterProfile
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec,
    private val authRepository: AuthRepository,
    private val schoolAdminRepository: SchoolAdminRepository,
    private val roleRepository: RoleRepository,
    private val routerProfile: RouterProfile,
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
                val user = executeCoordinatorResult { authRepository.getCurrentUser() }
                val role =
                    executeCoordinatorResult { Result.success(roleRepository.getCurrentRole()) }
                _state.update {
                    it.copy(user = user.await(), role = role.await())
                }
            }
        }
    }

    private fun loadSchoolName() {
        val userId = _state.value.user?.userId ?: return
        executeWithResultLoadingSync(
            key = "load_school",
            block = { schoolAdminRepository.getSchool(userId) },
            onSuccess = { school ->
                _state.update { it.copy(school = school) }
            })
    }

    fun onAvatarClick() {

    }

    fun onRoleClick() {

    }

    private fun updateAvatar(uri: Uri) {
        viewModelScope.launch {
            executeWithResultLoadingSync(
                key = "update_avatar",
                block = { Result.success(Unit) },
                onSuccess = { newAvatarUrl ->
                    loadCurrentUser()
                })
        }
    }

    private fun updateRole(newRole: Role) {
        viewModelScope.launch {
            executeWithResultLoadingSync(
                key = "update_role",
                block = { authRepository.saveRole(newRole) },
                onSuccess = {
                    loadCurrentUser()
                })
        }
    }

    fun logout() {
        executeWithResultLoadingSync("logout", { authRepository.logout() }, { routerProfile.toLogout() })
    }
}