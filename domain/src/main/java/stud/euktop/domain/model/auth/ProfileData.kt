package stud.euktop.domain.model.auth

import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Gender
import stud.euktop.domain.utils.validation.EmailValidator
import stud.euktop.domain.utils.validation.NameLetterOnlyOrNullValidator
import stud.euktop.domain.utils.validation.NameLetterOnlyValidator
import stud.euktop.domain.utils.validation.PhoneValidator
import java.util.Date

/**
 * Данные для редактирования профиля (используется в форме регистрации/редактирования).
 * Содержит валидаторы для каждого поля.
 */
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