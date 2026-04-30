package stud.euktop.schooljournal.presentation.main.admin.users

import stud.euktop.domain.model.AccountStatus
import stud.euktop.domain.model.Role
import stud.euktop.domain.model.School
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
    val availableRoles: List<Role> = emptyList(),
    val availableSchools: List<School> = emptyList(),
    val selectedRole: Role? = null,
    val selectedSchoolId: Int? = null,
) : BaseState<UserEditState>() {

    fun isEditMode() = userId != 0

    fun isFormValid(): Boolean {
        val baseValid = Validator.isAllValidate(lastName, firstName, surName, email, phone)
        if (!baseValid) return false
        val passwordValid = if (isEditMode()) {
            password.value.isNullOrBlank() || password.validate()
        } else {
            !password.value.isNullOrBlank() && password.validate()
        }
        if (!passwordValid) return false
        if (selectedRole != null) {
            return if (selectedRole == Role.ADMIN) true
            else selectedSchoolId != null
        }
        return false
    }

    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}