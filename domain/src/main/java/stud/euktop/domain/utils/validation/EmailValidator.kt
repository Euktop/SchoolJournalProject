package stud.euktop.domain.utils.validation

class EmailValidator(override var value: String? = "") : BaseValidator<EmailValidator>(value) {

    override fun _validate(value: String?): Boolean {
        // NEW: более понятная проверка на null/blank
        if (value.isNullOrBlank()) return false
        val pattern = "^[A-z0-9]+@[a-z0-9]+\\.[a-z0-9]+$".toRegex()
        return pattern.matches(value)
    }

    override fun copy(value: String?): EmailValidator = EmailValidator(value)

    // NEW: сообщение о причине ошибки
    override fun getValidationErrorMessage(value: String?): String? {
        return when {
            value.isNullOrBlank() -> "Email address is empty or blank"
            !"^[A-z0-9]+@[a-z0-9]+\\.[a-z0-9]+$".toRegex().matches(value) ->
                "Email format is invalid. Expected: user@domain.com"
            else -> null
        }
    }
}