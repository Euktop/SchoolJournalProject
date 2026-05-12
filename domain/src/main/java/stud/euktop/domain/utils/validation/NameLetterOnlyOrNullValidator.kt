package stud.euktop.domain.utils.validation

class NameLetterOnlyOrNullValidator(
    override var value: String? = ""
) : BaseValidator<NameLetterOnlyOrNullValidator>(value) {

    // NEW: упрощённая логика
    override fun _validate(value: String?): Boolean {
        return value == null || value.all { it.isLetter() }
    }

    override fun copy(value: String?): NameLetterOnlyOrNullValidator =
        NameLetterOnlyOrNullValidator(value)

    // NEW: сообщение об ошибке
    override fun getValidationErrorMessage(value: String?): String? {
        return if (value != null && !value.all { it.isLetter() }) {
            "Name must contain only letters (or be null), but was: '$value'"
        } else null
    }
}