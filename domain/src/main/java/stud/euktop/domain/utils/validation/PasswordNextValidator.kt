package stud.euktop.domain.utils.validation

class PasswordNextValidator(
    override var value: String?=""
): BaseValidator<PasswordNextValidator>(value) {
    override fun _validate(value: String?): Boolean {
        return this.value==value
    }

    override fun copy(value: String?): PasswordNextValidator {
        return PasswordNextValidator(value)
    }
}