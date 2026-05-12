package stud.euktop.domain.utils.validation

abstract class BaseValidator<V : BaseValidator<V>>(
    override var value: String? = ""
) : Validator<String, V>() {

    override fun _validate(value: String?): Boolean {
        return value?.isBlank() != true
    }

    override fun getValidate() = value!!

    // NEW: сообщение для случая, когда строка пустая или null
    override fun getValidationErrorMessage(value: String?): String? {
        return if (value.isNullOrBlank()) "Value must not be blank or null" else null
    }
}