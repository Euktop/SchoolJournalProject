package stud.euktop.domain.utils.validation

class NameLetterOnlyValidator(
    override var value: String? = ""
): BaseValidator<NameLetterOnlyValidator>(value) {
    override fun _validate(value: String?): Boolean {
        if(value?.toCharArray()?.all { a-> a.isLetter() }!=true) return false
        return super.validate(value)
    }

    override fun copy(value: String?): NameLetterOnlyValidator {
        return NameLetterOnlyValidator(value)
    }

}