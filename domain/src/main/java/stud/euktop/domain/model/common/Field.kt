package stud.euktop.domain.model.common
data class Field<T>(
    val value: T? = null,
    val isUpdate: Boolean = false
) {
    val uValue: T?
        get() = value?.takeIf { isUpdate }
}