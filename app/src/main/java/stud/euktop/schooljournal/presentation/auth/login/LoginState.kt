package stud.euktop.schooljournal.presentation.auth.login

import stud.euktop.domain.utils.validation.EmailValidator
import stud.euktop.domain.utils.validation.PasswordValidator
import stud.euktop.domain.utils.validation.Validator
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class LoginState(
    val email: EmailValidator = EmailValidator(),
    val password: PasswordValidator = PasswordValidator(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<LoginState>() {
    fun isButtonActive() = Validator.isAllValidate(email, password)
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): LoginState = copy(loadingMap = loadingMap)
}