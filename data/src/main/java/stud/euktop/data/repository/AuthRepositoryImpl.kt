package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockUserDataSource
import stud.euktop.domain.model.auth.Profile
import stud.euktop.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    override suspend fun login(login: String, passwordHash: String): Result<Profile> {
        MockDelayService.delay()
        return Result.success(MockUserDataSource.currentUser)
    }

    override suspend fun registration(profile: Profile, password: String): Result<Profile> {
        MockDelayService.delay()
        return Result.success(MockUserDataSource.currentUser)
    }

    override suspend fun getCurrentUser(): Result<Profile> {
        MockDelayService.delay()
        return Result.success(MockUserDataSource.currentUser)
    }
}