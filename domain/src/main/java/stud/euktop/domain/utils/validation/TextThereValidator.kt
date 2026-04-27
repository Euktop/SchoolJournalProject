package stud.euktop.domain.utils.validation

class TextThereValidator(
    override var value: String? = ""
) :
    BaseValidator<TextThereValidator>(value) {
    override fun copy(value: String?): TextThereValidator = TextThereValidator(value)
}