// data/src/main/java/stud/euktop/data/mock/repository/AuthMockRepositoryImpl.kt
package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockUserDataSource
import stud.euktop.domain.model.common.DataError
import stud.euktop.domain.model.user.UserAuth
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.model.user.UserRole
import stud.euktop.domain.repository.AuthRepository
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthMockRepositoryImpl @Inject constructor() : AuthRepository {
    private var currentUser: UserProfile? = MockUserDataSource.currentUser

    override suspend fun login(login: String, passwordHash: String): Result<UserAuth> {
        MockDelayService.delay()
        val user = currentUser ?: return Result.failure(DataError.UserNotFound("User not found"))
        return Result.success(UserAuth(user.userId, "mock-token", listOf(1)))
    }

    override suspend fun registration(profile: UserProfile, password: String): Result<UserAuth> {
        MockDelayService.delay()
        val roles = profile.roles.map { UserRole(it.role, it.schoolId, Date()) }
        val created = MockUserDataSource.addUser(profile, roles)
        currentUser = created
        return Result.success(UserAuth(created.userId, "mock-token", roles.map { it.role.ordinal }))
    }

    override suspend fun getCurrentUser(): Result<UserProfile> {
        MockDelayService.delay()
        val user = currentUser ?: return Result.failure(DataError.UserNotFound("No user logged in"))
        return Result.success(user)
    }

    override suspend fun logout() {
        currentUser = null
    }
}