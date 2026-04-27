package stud.euktop.schooljournal.presentation.auth.login

import stud.euktop.domain.model.Login
import stud.euktop.domain.utils.validation.EmailValidator
import stud.euktop.domain.utils.validation.PasswordValidator
import stud.euktop.domain.utils.validation.Validator
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class LoginState(
    override val isLoading: Boolean = false,
    val emailValidator: EmailValidator = EmailValidator(),
    val passwordValidator: PasswordValidator = PasswordValidator()
) : BaseState<LoginState>() {
    fun isButtonActive() = Validator.isAllValidate(emailValidator, passwordValidator)
    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}