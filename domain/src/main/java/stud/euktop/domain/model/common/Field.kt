package stud.euktop.domain.model.common

sealed interface Field<out T> {
    object Null : Field<Nothing>
    data class Value<T>(val value: T) : Field<T>
}