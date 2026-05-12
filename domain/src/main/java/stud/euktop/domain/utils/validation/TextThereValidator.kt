package stud.euktop.domain.utils.validation

class TextThereValidator(
    override var value: String? = ""
) : BaseValidator<TextThereValidator>(value) {

    // NEW: явная проверка – строка не должна быть null, пустой или состоять из пробелов
    override fun _validate(value: String?): Boolean {
        return !value.isNullOrBlank()
    }

    override fun copy(value: String?): TextThereValidator = TextThereValidator(value)

    // NEW: сообщение
    override fun getValidationErrorMessage(value: String?): String? {
        return if (value.isNullOrBlank()) "Text cannot be empty or contain only whitespace" else null
    }
}