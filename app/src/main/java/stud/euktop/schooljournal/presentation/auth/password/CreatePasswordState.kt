package stud.euktop.schooljournal.presentation.auth.password

import stud.euktop.domain.utils.validation.PasswordValidator
import stud.euktop.domain.utils.validation.Validator
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.binding.CreatePasswordFormState

data class CreatePasswordState(
    override val password: PasswordValidator = PasswordValidator(),
    override val confirmPassword: String = "",
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<CreatePasswordState>(), CreatePasswordFormState {
    fun isNextActive() =
        Validator.isAllValidate(password) && password.value == confirmPassword

    override fun updateLoading(loadingMap: Map<String, Boolean>): CreatePasswordState =
        copy(loadingMap = loadingMap)
}
