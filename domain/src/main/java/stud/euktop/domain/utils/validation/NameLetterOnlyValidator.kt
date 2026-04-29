package stud.euktop.domain.utils.validation

class NameLetterOnlyValidator(
    override var value: String? = ""
): BaseValidator<NameLetterOnlyValidator>(value) {
    override fun _validate(value: String?): Boolean {
        return value?.toCharArray()?.all { a-> a.isLetter() } == true
    }

    override fun copy(value: String?): NameLetterOnlyValidator {
        return NameLetterOnlyValidator(value)
    }

}