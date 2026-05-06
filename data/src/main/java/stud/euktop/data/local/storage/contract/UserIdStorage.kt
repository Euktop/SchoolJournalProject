package stud.euktop.data.local.storage.contract


interface UserIdStorage: BaseStorage {
    suspend fun getUserId(): Int?
    suspend fun setUserId(userId: Int?)
}