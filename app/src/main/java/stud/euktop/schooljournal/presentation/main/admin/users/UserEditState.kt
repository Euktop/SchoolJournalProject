package stud.euktop.schooljournal.presentation.main.admin.users

import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.utils.validation.EmailValidator
import stud.euktop.domain.utils.validation.NameLetterOnlyValidator
import stud.euktop.domain.utils.validation.PasswordNullValidator
import stud.euktop.domain.utils.validation.PhoneValidator
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class UserEditState(
    val userId: Int = 0,
    val lastName: NameLetterOnlyValidator = NameLetterOnlyValidator(),
    val firstName: NameLetterOnlyValidator = NameLetterOnlyValidator(),
    val surName: NameLetterOnlyValidator = NameLetterOnlyValidator(),
    val email: EmailValidator = EmailValidator(),
    val phone: PhoneValidator = PhoneValidator(),
    val password: PasswordNullValidator = PasswordNullValidator(),
    val accountStatus: AccountStatus = AccountStatus.ACTIVE,
    val selectedRoles: List<RoleWithSchool> = emptyList(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<UserEditState>() {

    fun isFormValid(): Boolean =
        lastName.validate() && firstName.validate() && surName.validate() &&
                email.validate() && phone.validate() && password.validate()

    override fun updateIsLoading(loadingMap: Map<String, Boolean>): UserEditState {
        return copy(loadingMap = loadingMap)
    }
}

data class RoleWithSchool(
    val role: Role,
    val schoolId: Int?,
    val schoolName: String? = null
)