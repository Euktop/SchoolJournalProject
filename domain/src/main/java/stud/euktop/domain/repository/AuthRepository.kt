package stud.euktop.domain.repository

import stud.euktop.domain.model.user.UserInfo

interface AuthRepository {
    suspend fun login(login: String, passwordHash: String): Result<UserInfo>
    suspend fun registration(userInfo: UserInfo, password: String): Result<UserInfo>
    suspend fun getCurrentUser(): Result<UserInfo>
}