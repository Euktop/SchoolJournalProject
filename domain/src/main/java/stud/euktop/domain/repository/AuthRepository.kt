package stud.euktop.domain.repository

import stud.euktop.domain.model.Profile

interface AuthRepository {
    suspend fun login(login: String, passwordHash: String): Result<Unit>
    suspend fun registration(profile: Profile, password: String): Result<Profile>
}