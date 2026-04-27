package stud.euktop.network.interceptor

fun interface TokenProvider {
    fun getToken(): String?
}