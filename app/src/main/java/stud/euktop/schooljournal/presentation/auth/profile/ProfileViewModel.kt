package stud.euktop.schooljournal.presentation.auth.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.user.Gender
import stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.binding.ProfileFormActions
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAuth
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authCoordinator: AuthCoordinator,
    private val routerAuth: RouterAuth,
    executeCoordinatorExec: CoordinatorExec
) : BaseViewModel<ProfileState, Unit>(), ProfileFormActions {
    init {
        this.executeCoordinator = executeCoordinatorExec
    }

    override fun initState() = ProfileState()

    override fun lastNameSet(value: String) {
        _state.update { it.copy(lastName = it.lastName.copy(value)) }
    }

    override fun firstNameSet(value: String) {
        _state.update { it.copy(firstName = it.firstName.copy(value)) }
    }

    override fun surNameSet(value: String) {
        _state.update { it.copy(surName = it.surName.copy(value)) }
    }

    override fun genderSet(value: Gender?) {
        _state.update { it.copy(gender = value) }
    }

    override fun birthDaySet(value: Date?) {
        _state.update { it.copy(birthDay = value) }
    }

    override fun emailSet(value: String) {
        _state.update { it.copy(email = it.email.copy(value)) }
    }

    override fun phoneSet(value: String) {
        _state.update { it.copy(phone = it.phone.copy(value)) }
    }


    fun onNextClick() {
        if (!_state.value.isButtonActive()) return
        executeWithResultLoadingSync(
            key = "save_profile",
            block = {
                authCoordinator.saveProfile(
                    lastName = _state.value.lastName.getValidate(),
                    firstName = _state.value.firstName.getValidate(),
                    surName = _state.value.surName.value ?: "",
                    gender = _state.value.gender ?: Gender.NONE,
                    birthDay = _state.value.birthDay ?: Date(),
                    email = _state.value.email.getValidate(),
                    phone = _state.value.phone.getValidate()
                )
            },
            onSuccess = { routerAuth.toCreatePassword() }
        )
    }
}