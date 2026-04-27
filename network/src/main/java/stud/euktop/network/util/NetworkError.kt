package stud.euktop.network.util

sealed class NetworkError : Throwable() {
    data class HttpError(val code: Int, override val message: String?) : NetworkError()
    data class NetworkConnection(override val message: String?) : NetworkError()
    object EmptyBody : NetworkError()

    data class Unknown(override val message: String?) : NetworkError()
}