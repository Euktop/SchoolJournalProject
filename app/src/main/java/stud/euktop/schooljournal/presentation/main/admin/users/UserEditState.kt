package stud.euktop.schooljournal.presentation.main.admin.users

import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.RoleSchools
import stud.euktop.domain.utils.validation.EmailValidator
import stud.euktop.domain.utils.validation.NameLetterOnlyOrNullValidator
import stud.euktop.domain.utils.validation.NameLetterOnlyValidator
import stud.euktop.domain.utils.validation.PasswordNullValidator
import stud.euktop.domain.utils.validation.PhoneValidator
import stud.euktop.domain.utils.validation.Validator
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class UserEditState(
    override val isLoading: Boolean = false,
    val userId: Int = 0,
    val lastName: NameLetterOnlyValidator = NameLetterOnlyValidator(),
    val firstName: NameLetterOnlyValidator = NameLetterOnlyValidator(),
    val surName: NameLetterOnlyOrNullValidator = NameLetterOnlyOrNullValidator(),
    val email: EmailValidator = EmailValidator(),
    val phone: PhoneValidator = PhoneValidator(),
    val password: PasswordNullValidator = PasswordNullValidator(),
    val accountStatus: AccountStatus = AccountStatus.ACTIVE,
    val selectedRoles: List<RoleSchools> = emptyList()
) : BaseState<UserEditState>() {

    fun isEditMode() = userId != 0

    fun isFormValid() =
        Validator.isAllValidate(lastName, firstName, surName, email, phone, password)

    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}