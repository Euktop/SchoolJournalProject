package stud.euktop.domain.utils.validation

class NameLetterOnlyOrNullValidator(
    override var value: String? = ""
) : BaseValidator<NameLetterOnlyOrNullValidator>(value) {
    override fun _validate(value: String?): Boolean {
        if (value != null && !value.toCharArray().all { a -> a.isLetter() }) return false
        return super.validate(value)
    }

    override fun copy(value: String?): NameLetterOnlyOrNullValidator {
        return NameLetterOnlyOrNullValidator(value)
    }
}