package stud.euktop.domain.model

import stud.euktop.domain.utils.validation.EmailValidator
import stud.euktop.domain.utils.validation.NameLetterOnlyOrNullValidator
import stud.euktop.domain.utils.validation.NameLetterOnlyValidator
import stud.euktop.domain.utils.validation.PhoneValidator
import java.util.Date

data class ProfileData(
    val lastName: NameLetterOnlyValidator = NameLetterOnlyValidator(),
    val firstName: NameLetterOnlyValidator = NameLetterOnlyValidator(),
    val surName: NameLetterOnlyOrNullValidator = NameLetterOnlyOrNullValidator(),
    val gender: Gender = Gender.NONE,
    val birthDay: Date? = null,
    val email: EmailValidator = EmailValidator(),
    val phone: PhoneValidator = PhoneValidator(),
    val dateRegistration: Date,
    val accountStatusId: AccountStatus,
    val roles: List<Role>
)