package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockUserDataSource
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.common.DataError
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.model.user.UserRole
import stud.euktop.domain.repository.AuthRepository
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthMockRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler
) : AuthRepository {
    private var currentUser: UserProfile? = MockUserDataSource.currentUser

    override suspend fun login(login: String, passwordHash: String): Result<Unit> {
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            if (currentUser == null) throw DataError.UserNotFound("User not found")
        }
    }

    override suspend fun registration(profile: UserProfile, password: String): Result<Unit> {
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val roles = profile.roles.map { UserRole(it.role, it.schoolId, Date()) }
            val created = MockUserDataSource.addUser(profile, roles)
            currentUser = created
        }
    }

    override suspend fun getCurrentUser(): Result<UserProfile> {
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            currentUser ?: throw DataError.UserNotFound("No user logged in")
        }
    }

    override suspend fun logout(): Result<Unit> {
        return apiErrorHandler.safeApiCall {
            currentUser = null
        }
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> {
        return apiErrorHandler.safeApiCall {
        }
    }

    override suspend fun getRoles(): Result<List<Role>> {
        return Result.success(listOf(Role.ADMIN, Role.TEACHER, Role.STUDENT))
    }

    override suspend fun saveRole(role: Role): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getSaveRole(): Result<Role?> {
        return Result.success(Role.ADMIN)
    }
}