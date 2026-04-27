package stud.euktop.domain.model

sealed class DataError : Throwable() {
    data class HttpError(val code: Int, override val message: String?) : DataError()
    data class NetworkConnection(override val message: String?) : DataError()
    object EmptyBody : DataError()

    data class Unknown(override val message: String?) : DataError()
}