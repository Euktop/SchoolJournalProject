package stud.euktop.domain.utils.validation

class PasswordValidator(
    override var value: String? = null
): BaseValidator<PasswordValidator>(value) {
    override fun _validate(value: String?): Boolean {
        return value!=null && value.length>=8 &&
                value.any { c->c.isUpperCase() }&&
                value.any { c->c.isLowerCase() }&&
                value.any { c->c.isDigit() }&&
                value.any { c->!c.isLetterOrDigit()}
    }

    override fun copy(value: String?): PasswordValidator {
        return PasswordValidator(value)
    }
}