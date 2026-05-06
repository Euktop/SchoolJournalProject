package stud.euktop.data.local.storage.contract

interface TokenStorage : BaseStorage {
    suspend fun getToken(): String?
    suspend fun setToken(token: String?)
}