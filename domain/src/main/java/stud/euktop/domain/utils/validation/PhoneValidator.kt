package stud.euktop.domain.utils.validation

class PhoneValidator(override var value: String? = null) : BaseValidator<PhoneValidator>() {

    override fun copy(value: String?): PhoneValidator = PhoneValidator(value)

    override fun _validate(value: String?): Boolean {
        return value.isNullOrEmpty() || "^\\+7[0-9]{10}$".toRegex().matches(value)
    }

    // NEW: сообщение
    override fun getValidationErrorMessage(value: String?): String? {
        if (!value.isNullOrEmpty() && !"^\\+7[0-9]{10}$".toRegex().matches(value)) {
            return "Phone number must be in format +7XXXXXXXXXX (10 digits after +7), but was: '$value'"
        }
        return null
    }
}