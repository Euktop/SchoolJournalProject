package stud.euktop.schooljournal.presentation.auth.profile

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.model.Gender
import stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel для экрана регистрации.
 *
 * Назначение: управляет валидацией полей профиля и временным хранением данных
 * через AuthCoordinator.
 *
 * Функционал:
 * - State: поля lastName, firstName, surName, gender, birthDay, email, phone
 * - Методы обновления каждого поля
 * - Валидация всех полей через соответствующие валидаторы
 * - onNextClick() – сохраняет профиль через AuthCoordinator.saveProfile
 * - После сохранения отправляет событие Unit для перехода к CreatePasswordFragment
 *
 * @see ProfileFragment
 * @see AuthCoordinator
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authCoordinator: AuthCoordinator
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

    fun onNextClick() {
        executeLoadingBlockSync(
            block = {
                authCoordinator.saveProfile(
                    lastName = state.value.lastName.getValidate(),
                    firstName = state.value.firstName.getValidate(),
                    surName = state.value.surName.getValidate(),
                    gender = state.value.gender ?: Gender.NONE,
                    birthDay = state.value.birthDay ?: Date(),
                    email = state.value.email.getValidate(),
                    phone = state.value.phone.getValidate()
                )
            }
        )
        viewModelScope.launch { _event.emit(Unit) }
    }
}