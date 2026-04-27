package stud.euktop.schooljournal.presentation.auth.profile

import stud.euktop.domain.model.Gender
import stud.euktop.domain.utils.validation.EmailValidator
import stud.euktop.domain.utils.validation.NameLetterOnlyValidator
import stud.euktop.domain.utils.validation.PhoneValidator
import stud.euktop.domain.utils.validation.Validator
import stud.euktop.schooljournal.presentation.common.base.BaseState
import java.util.Date

data class ProfileState(
    override val isLoading: Boolean = false,
    val lastName: NameLetterOnlyValidator = NameLetterOnlyValidator(),
    val firstName: NameLetterOnlyValidator = NameLetterOnlyValidator(),
    val surName: NameLetterOnlyValidator = NameLetterOnlyValidator(),
    val gender: Gender? = null,
    val birthDay: Date? = null,
    val email: EmailValidator = EmailValidator(),
    val phone: PhoneValidator = PhoneValidator(),
) : BaseState<ProfileState>() {
    fun isButtonActive() = Validator.isAllValidate(lastName, firstName, surName, email, phone)
            && Validator.isAllNullValidate(gender, birthDay)

    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}