package stud.euktop.domain.utils.validation

class PhoneValidator(override var value: String? = null) : BaseValidator<PhoneValidator>() {
    override fun copy(value: String?): PhoneValidator {
        return PhoneValidator(value)
    }

    override fun _validate(value: String?): Boolean {
        return value.isNullOrEmpty() || "^(\\+7)[0-9]{10}$".toRegex().matches(value)
    }
}