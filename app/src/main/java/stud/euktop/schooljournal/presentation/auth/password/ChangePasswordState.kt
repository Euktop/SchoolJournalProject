package stud.euktop.schooljournal.presentation.auth.password

import stud.euktop.domain.utils.validation.PasswordValidator
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class ChangePasswordState(
    val oldPassword: PasswordValidator = PasswordValidator(),
    val newPassword: PasswordValidator = PasswordValidator(),
    val confirmPassword: String = "",
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<ChangePasswordState>() {
    fun isFormValid(): Boolean =
        oldPassword.validate() && newPassword.validate() && newPassword.value == confirmPassword

    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}