package stud.euktop.domain.utils.validation

/**
 *
 */
abstract class BaseValidator<V : BaseValidator<V>>(
    override var value: String? = ""
) : Validator<String, V>() {
    override fun _validate(value: String?): Boolean {
        return value?.isBlank() != true
    }

    override fun getValidate() = value!!
}