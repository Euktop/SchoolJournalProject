package stud.euktop.schooljournal.presentation.main.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

// ProfileViewModel.kt
@HiltViewModel
class ProfileViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager,
    private val authRepository: AuthRepository
) : BaseViewModel<ProfileState, Unit>() {
    override fun initState() = ProfileState()

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        loadProfile()
    }

    fun loadProfile() {
        executeWithCoordinatorAndLoadingSync(
            block = { authRepository.getCurrentUser() },
            onSuccess = { profile ->
                _state.update { it ->
                    it.copy(
                        userName = "${profile.lastName} ${profile.firstName}",
                        email = profile.email,
                        roleNames = profile.roles.map { r ->
                            r.name.lowercase().replaceFirstChar { it.uppercase() }
                        },
                        isLoading = false
                    )
                }
            }
        )
    }
}