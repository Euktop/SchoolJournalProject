package stud.euktop.domain.utils.validation

class PasswordNullValidator(override var value: String? = null) :
    BaseValidator<PasswordNullValidator>() {
    override fun _validate(value: String?): Boolean {
        return value == null || PasswordValidator(value).validate()
    }

    override fun copy(value: String?) = PasswordNullValidator(value)
}