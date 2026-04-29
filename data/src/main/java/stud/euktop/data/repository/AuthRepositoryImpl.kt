package stud.euktop.data.repository

import stud.euktop.data.MockData
import stud.euktop.domain.model.Profile
import stud.euktop.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    override suspend fun login(
        login: String, passwordHash: String
    ): Result<Profile> {
        MockData.delay()
        return Result.success(MockData.currentUser)
    }

    override suspend fun registration(
        profile: Profile, password: String
    ): Result<Profile> {
        MockData.delay()
        return Result.success(MockData.currentUser)
    }

    override suspend fun getCurrentUser(): Result<Profile> {
        MockData.delay()
        return Result.success(MockData.currentUser)
    }
}