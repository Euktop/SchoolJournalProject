package stud.euktop.schooljournal.presentation.main.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterProfile
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec,
    private val authRepository: AuthRepository,
    private val routerProfile: RouterProfile
) : BaseViewModel<ProfileState, Unit>() {

    override fun initState() = ProfileState()

    init {
        executeCoordinator = coordinatorExec
        onResume()
    }

    fun logout() {
        executeWithLoadingSync(
            "logout",
            { authRepository.logout() },
            { routerProfile.toLogout() } // <-- Навигируем на экран входа
        )
    }

    fun onResume() {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        executeWithLoadingSync(
            key = "load_profile",
            block = { authRepository.getCurrentUser() },
            onSuccess = { profile ->
                _state.update { it.copy(user = profile) }
            }
        )
    }
}