package stud.euktop.domain.utils.validation

class PhoneValidator(override var value: String? = null) : BaseValidator<PhoneValidator>() {

    override fun copy(value: String?): PhoneValidator = PhoneValidator(value)

    override fun _validate(value: String?): Boolean {
        return value.isNullOrEmpty() || "^\\+?[0-9][0-9-\\s()]{7,14}$".toRegex().matches(value)
    }

    override fun getValidate(): String {
        val cleaned = value!!.replace(Regex("[^0-9]"), "")
        return formatPhoneNumberSimple(cleaned)
    }

    fun formatPhoneNumberSimple(digitsOnly: String): String {
        var number = digitsOnly

        if (number.startsWith("8") && number.length == 11) {
            number = "7" + number.substring(1)
        }

        if (number.startsWith("0") && number.length == 11) {
            number = "7" + number.substring(1)
        }

        if (number.length == 10) {
            number = "7$number"
        }

        if (number.length == 11) {
            return "+${number[0]} ${number[1]}${number[2]}${number[3]} ${number[4]}${number[5]}${number[6]} ${number[7]}${number[8]} ${number[9]}${number[10]}"
        }

        return if (number.startsWith("7")) "+$number" else "+$number"
    }

    // NEW: сообщение
    override fun getValidationErrorMessage(value: String?): String? {
        if (!value.isNullOrEmpty() && !"^\\+?[0-9][0-9-\\s()]{7,14}$".toRegex().matches(value)) {
            return "Phone number must be in format +7XXXXXXXXXX (10 digits after +7), but was: '$value'"
        }
        return null
    }
}