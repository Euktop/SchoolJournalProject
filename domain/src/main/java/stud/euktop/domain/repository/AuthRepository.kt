package stud.euktop.domain.repository

import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserProfile

interface AuthRepository {
    suspend fun login(login: String, passwordHash: String): Result<Unit>
    suspend fun registration(profile: UserProfile, password: String): Result<Unit>
    suspend fun getCurrentUser(): Result<UserProfile>
    suspend fun logout(): Result<Unit>
    suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Result<Unit>   // добавлено

    suspend fun getRoles(): Result<List<Role>>
    suspend fun saveRole(role: Role): Result<Unit>
    suspend fun getSaveRole(): Result<Role?>
}