package stud.euktop.schooljournal.presentation.main.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec,
    private val authRepository: AuthRepository,
) : BaseViewModel<ProfileState, Unit>() {

    override fun initState() = ProfileState()

    init {
        executeCoordinator = coordinatorExec
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