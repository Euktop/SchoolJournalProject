package stud.euktop.schooljournal.presentation.auth.profile

import stud.euktop.domain.model.user.Gender
import stud.euktop.domain.utils.validation.EmailValidator
import stud.euktop.domain.utils.validation.NameLetterOnlyValidator
import stud.euktop.domain.utils.validation.PhoneValidator
import stud.euktop.domain.utils.validation.Validator
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.binding.ProfileFormState
import java.util.Date


data class ProfileState(
    override val lastName: NameLetterOnlyValidator = NameLetterOnlyValidator(),
    override val firstName: NameLetterOnlyValidator = NameLetterOnlyValidator(),
    override val surName: NameLetterOnlyValidator = NameLetterOnlyValidator(),
    override val gender: Gender? = null,
    override val birthDay: Date? = null,
    override val email: EmailValidator = EmailValidator(),
    override val phone: PhoneValidator = PhoneValidator(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<ProfileState>(), ProfileFormState {
    fun isButtonActive(): Boolean =
        Validator.isAllValidate(lastName, firstName, surName, email, phone)
                && gender != null && birthDay != null

    override fun updateIsLoading(loadingMap: Map<String, Boolean>): ProfileState =
        copy(loadingMap = loadingMap)
}