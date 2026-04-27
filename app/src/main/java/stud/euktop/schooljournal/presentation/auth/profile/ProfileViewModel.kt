package stud.euktop.schooljournal.presentation.auth.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.Gender
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
) : BaseViewModel<ProfileState, Unit>() {
    override fun initState() = ProfileState()

    fun lastNameSet(value: String) {
        _state.update { it.copy(lastName = it.lastName.copy(value)) }
    }

    fun firstNameSet(value: String) {
        _state.update { it.copy(firstName = it.firstName.copy(value)) }
    }

    fun surNameSet(value: String) {
        _state.update { it.copy(surName = it.surName.copy(value)) }
    }

    fun genderSet(value: Gender) {
        _state.update { it.copy(gender = value) }
    }

    fun birthDaySet(value: Date) {
        _state.update { it.copy(birthDay = value) }
    }

    fun emailSet(value: String) {
        _state.update { it.copy(email = it.email.copy(value)) }
    }

    fun phoneSet(value: String) {
        _state.update { it.copy(phone = it.phone.copy(value)) }
    }
}