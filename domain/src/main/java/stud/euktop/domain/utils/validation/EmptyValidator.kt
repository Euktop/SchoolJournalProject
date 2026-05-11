package stud.euktop.domain.utils.validation

class EmptyValidator<T>(override var value: T? = null) : Validator<T, EmptyValidator<T>>() {
    override fun _validate(value: T?) = true

    override fun copy(value: T?): EmptyValidator<T> = EmptyValidator(value)

    override fun getValidate(): T = value!!
}