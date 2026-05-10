package stud.euktop.domain.repository

import stud.euktop.domain.model.user.UserProfile

interface AuthRepository {
    suspend fun login(login: String, passwordHash: String): Result<Unit>
    suspend fun registration(profile: UserProfile, password: String): Result<Unit>
    suspend fun getCurrentUser(): Result<UserProfile>
    suspend fun logout()
}