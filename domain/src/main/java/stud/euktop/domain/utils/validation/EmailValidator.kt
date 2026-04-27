package stud.euktop.domain.utils.validation

class EmailValidator(override var value: String? = ""): BaseValidator<EmailValidator>(value) {
    override fun _validate(value: String?): Boolean {
        if(value?.isBlank()!=false) return false
        val pattern = "^[a-z0-9]+@[a-z0-9]+\\.[a-z0-9]+$".toRegex()
        val isValid = pattern.matches(value)
        return isValid
    }

    override fun copy(value: String?): EmailValidator {
        return EmailValidator(value)
    }
}