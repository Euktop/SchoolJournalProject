package stud.euktop.domain.utils.validation

class PasswordNullValidator(override var value: String? = null) :
    BaseValidator<PasswordNullValidator>() {

    override fun _validate(value: String?): Boolean {
        return value == null || PasswordValidator(value).validate()
    }

    override fun copy(value: String?) = PasswordNullValidator(value)

    // NEW: сообщение для невалидного пароля (когда value != null)
    override fun getValidationErrorMessage(value: String?): String? {
        if (value == null) return null
        return when {
            value.length < 8 -> "Password must be at least 8 characters long"
            !value.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
            !value.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter"
            !value.any { it.isDigit() } -> "Password must contain at least one digit"
            !value.any { !it.isLetterOrDigit() } -> "Password must contain at least one special character"
            else -> null
        }
    }
}