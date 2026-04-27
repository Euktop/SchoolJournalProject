package stud.euktop.schooljournal.presentation.auth.password

import stud.euktop.domain.utils.validation.PasswordNextValidator
import stud.euktop.domain.utils.validation.PasswordValidator
import stud.euktop.domain.utils.validation.Validator
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class CreatePasswordState(
    override val isLoading: Boolean = false,
    val passwordValidator: PasswordValidator = PasswordValidator(),
    val nextPasswordNextValidator: String = ""
) : BaseState<CreatePasswordState>() {
    fun isNextActive() =
        Validator.isAllValidate(passwordValidator) && passwordValidator.value == nextPasswordNextValidator

    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}
