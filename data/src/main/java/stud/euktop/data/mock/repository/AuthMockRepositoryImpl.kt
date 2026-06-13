// data/src/main/java/stud/euktop/data/mock/repository/AuthMockRepositoryImpl.kt
package stud.euktop.data.mock.repository

import stud.euktop.data.local.storage.contract.RoleStorage
import stud.euktop.data.local.storage.contract.TokenStorage
import stud.euktop.data.local.storage.contract.UserIdStorage
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockUserDataSource
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.model.user.UserRole
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthMockRepositoryImpl @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val userIdStorage: UserIdStorage,
    private val roleStorage: RoleStorage,
    private val apiErrorHandler: ApiErrorHandler
) : AuthRepository {
    private val tag = this.toSimpleTag()

    override suspend fun login(login: String, passwordHash: String): Result<Unit> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "login", "Attempting login for: $login")
            MockDelayService.delay()

            val mockUser =
                MockUserDataSource.getAllUsersWithRoles().firstOrNull { it.email == login }
                    ?: UserProfile.createObject(
                        email = login,
                        lastName = "Mock",
                        firstName = "User",
                        accountStatus = stud.euktop.domain.model.user.AccountStatus.PENDING
                    )

            tokenStorage.setToken("mock_jwt_token_1234567890")
            userIdStorage.setUserId(mockUser.userId)
            roleStorage.saveRole(mockUser.roles.firstOrNull()?.role ?: Role.STUDENT)

            logger?.i(tag, "login_success", "User ${mockUser.userId} logged in successfully")
            Unit
        }

    override suspend fun registration(profile: UserProfile, password: String): Result<Unit> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "registration", "Registering new user: ${profile.email}")
            MockDelayService.delay()

            val roles = profile.roles.map { UserRole(it.role, it.schoolId, Date()) }
            val created = MockUserDataSource.addUser(profile, roles)

            tokenStorage.setToken("mock_jwt_token_new_user")
            userIdStorage.setUserId(created.userId)
            roleStorage.saveRole(created.roles.firstOrNull()?.role ?: Role.STUDENT)

            logger?.i(
                tag,
                "registration_success",
                "User ${created.userId} registered and logged in"
            )
            Unit
        }

    override suspend fun getCurrentUser(): Result<UserProfile> =
        apiErrorHandler.safeApiCall {
            logger?.d(tag, "getCurrentUser", "Fetching current user")
            MockDelayService.delay()

            val userId = userIdStorage.getUserId()
            if (userId != null) {
                val user = MockUserDataSource.getUser(userId)
                    ?: UserProfile.createObject(
                        userId = userId,
                        lastName = "Unknown",
                        firstName = "User",
                        accountStatus = stud.euktop.domain.model.user.AccountStatus.PENDING
                    )
                logger?.i(tag, "getCurrentUser_success", "Returned user: ${user.userId}")
                user
            } else {
                logger?.e(
                    tag,
                    "getCurrentUser_error",
                    null,
                    "No user ID in storage, returning guest fallback"
                )
                UserProfile.createObject(
                    lastName = "Guest",
                    firstName = "User",
                    accountStatus = stud.euktop.domain.model.user.AccountStatus.PENDING
                )
            }
        }

    override suspend fun logout(): Result<Unit> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "logout", "Clearing session storage")
            MockDelayService.delay()
            tokenStorage.clearAll()
            userIdStorage.clearAll()
            roleStorage.clearAll()
            Unit
        }

    override suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "changePassword", "Password changed successfully (mock)")
            MockDelayService.delay()
            Unit
        }

    override suspend fun getRoles(): Result<List<Role>> =
        apiErrorHandler.safeApiCall {
            logger?.d(tag, "getRoles", "Fetching available roles")
            MockDelayService.delay()
            listOf(Role.ADMIN, Role.TEACHER, Role.STUDENT, Role.PARENT, Role.DIRECTOR)
        }

    override suspend fun saveRole(role: Role): Result<Unit> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "saveRole", "Saving role: $role")
            roleStorage.saveRole(role)
            Unit
        }

    override suspend fun getSaveRole(): Result<Role?> =
        apiErrorHandler.safeApiCall {
            logger?.d(tag, "getSaveRole", "Fetching saved role")
            roleStorage.getRole() ?: Role.STUDENT // Fallback вместо null
        }
}