package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockUserDataSource
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.model.common.DataError
import stud.euktop.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    override suspend fun login(login: String, passwordHash: String): Result<UserInfo> {
        MockDelayService.delay()
        return Result.success(MockUserDataSource.currentUser)
    }

    override suspend fun registration(userInfo: UserInfo, password: String): Result<UserInfo> {
        MockDelayService.delay()
        return Result.success(MockUserDataSource.currentUser)
    }

    override suspend fun getCurrentUser(): Result<UserInfo> {
        MockDelayService.delay()
        return MockUserDataSource.getUser(1)?.let { Result.success(it) }
            ?: Result.failure(DataError.Unknown(null))
    }
}