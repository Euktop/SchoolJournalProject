package stud.euktop.domain.utils.validation

open class PasswordValidator(
    override var value: String? = null
) : BaseValidator<PasswordValidator>(value) {

    override fun _validate(value: String?): Boolean {
        return value != null && value.length >= 8 &&
                value.any { it.isUpperCase() } &&
                value.any { it.isLowerCase() } &&
                value.any { it.isDigit() } &&
                value.any { !it.isLetterOrDigit() }
    }

    override fun copy(value: String?): PasswordValidator = PasswordValidator(value)

    // NEW: сообщение
    override fun getValidationErrorMessage(value: String?): String? {
        if (value == null) return "Password cannot be null"
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