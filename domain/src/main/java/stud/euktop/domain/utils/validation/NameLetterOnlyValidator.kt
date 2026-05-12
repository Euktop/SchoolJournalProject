package stud.euktop.domain.utils.validation

class NameLetterOnlyValidator(
    override var value: String? = ""
) : BaseValidator<NameLetterOnlyValidator>(value) {

    override fun _validate(value: String?): Boolean {
        return value?.all { it.isLetter() } == true
    }

    override fun copy(value: String?): NameLetterOnlyValidator =
        NameLetterOnlyValidator(value)

    // NEW: сообщение
    override fun getValidationErrorMessage(value: String?): String? {
        return when {
            value.isNullOrBlank() -> "Name cannot be null or empty"
            !value.all { it.isLetter() } -> "Name must contain only letters, but was: '$value'"
            else -> null
        }
    }
}