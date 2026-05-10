package stud.euktop.domain.utils.validation

interface ValidatorInterface<T> {
    var value: T?
    fun validate(value: T? = this.value): Boolean
    fun getValidate(): T

}