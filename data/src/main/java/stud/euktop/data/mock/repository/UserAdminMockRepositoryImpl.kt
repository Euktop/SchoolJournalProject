// data/src/main/java/stud/euktop/data/mock/repository/UserAdminMockRepositoryImpl.kt
package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockUserDataSource
import stud.euktop.data.mock.data.fullName
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.model.user.UserRole
import stud.euktop.domain.model.user.UserUpdate
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAdminMockRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler
) : UserAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getUsers(filter: UserFilter): Result<List<UserListItem>> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "getUsers", "filter=$filter")
            MockDelayService.delay()
            val users = MockUserDataSource.getAllUsersWithRoles()
            val filtered = users.filter { user ->
                (filter.fullName == null || user.fullName()
                    .contains(filter.fullName!!, ignoreCase = true)) &&
                        (filter.role == null || user.roles.any { it.role == filter.role }) &&
                        (filter.accountStatus == null || user.accountStatus == filter.accountStatus)
            }.map {
                UserListItem.createObject(
                    userId = it.userId,
                    lastName = it.lastName,
                    firstName = it.firstName,
                    surName = it.surName,
                    email = it.email,
                    accountStatus = it.accountStatus
                )
            }
            val result = filtered.drop(filter.pagination.offset ?: 0)
                .take(filter.pagination.limit ?: Int.MAX_VALUE)
            logger?.i(tag, "getUsers_success", "Returned ${result.size} users")
            result.ifEmpty {
                listOf(
                    UserListItem.createObject(
                        userId = 0,
                        lastName = "Неизвестный",
                        firstName = "Пользователь",
                        surName = null,
                        email = "unknown@mock.local",
                        accountStatus = AccountStatus.DELETED
                    )
                )
            }
        }

    override suspend fun getUser(userId: Int): Result<UserProfile> = apiErrorHandler.safeApiCall {
        logger?.i(tag, "getUser", "userId=$userId")
        MockDelayService.delay()
        MockUserDataSource.getUser(userId) ?: UserProfile.createObject(
            userId = userId,
            lastName = "Неизвестный",
            firstName = "Пользователь",
            email = "unknown@mock.local",
            accountStatus = AccountStatus.DELETED
        )
    }

    override suspend fun addUser(profile: UserProfile, password: String?): Result<UserProfile> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "addUser", "Adding user: ${profile.email}")
            MockDelayService.delay()
            val newUser = profile.copy(userId = 0, dateRegistration = Date())
            val created = MockUserDataSource.addUser(newUser, profile.roles)
            logger?.i(tag, "addUser_success", "Created user with ID: ${created.userId}")
            created
        }

    override suspend fun updateUser(update: UserUpdate): Result<UserProfile> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "updateUser", "update=$update")
            MockDelayService.delay()
            val existing = MockUserDataSource.getUser(update.userId) ?: UserProfile.createObject(
                userId = update.userId
            )
            val updated = existing.copy(
                lastName = update.lastName.uValue ?: existing.lastName,
                firstName = update.firstName.uValue ?: existing.firstName,
                surName = update.surName.uValue ?: existing.surName,
                gender = update.gender.uValue ?: existing.gender,
                birthday = update.birthday.uValue ?: existing.birthday,
                email = update.email.uValue ?: existing.email,
                phone = update.phone.uValue ?: existing.phone,
                accountStatus = update.accountStatus.uValue ?: existing.accountStatus
            )
            MockUserDataSource.updateUser(updated)
            logger?.i(tag, "updateUser_success", "User ${updated.userId} updated")
            updated
        }

    override suspend fun deleteUser(userId: Int): Result<Unit> = apiErrorHandler.safeApiCall {
        logger?.i(tag, "deleteUser", "userId=$userId")
        MockDelayService.delay()
        val removed = MockUserDataSource.deleteUser(userId)
        if (!removed) {
            logger?.d(
                tag,
                "deleteUser_warning",
                "User not found, returning success for idempotency"
            )
        }
        Unit
    }

    override suspend fun addUserRole(userId: Int, role: Role, schoolId: Int?): Result<UserRole> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "addUserRole", "userId=$userId, role=$role")
            MockDelayService.delay()
            val user =
                MockUserDataSource.getUser(userId) ?: UserProfile.createObject(userId = userId)
            val newRole = UserRole(role, schoolId, Date())
            val updatedUser = user.copy(roles = user.roles + newRole)
            MockUserDataSource.updateUser(updatedUser)
            logger?.i(tag, "addUserRole_success", "Role $role added to user $userId")
            newRole
        }

    override suspend fun removeRole(userId: Int, role: Role, schoolId: Int?): Result<Unit> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "removeRole", "userId=$userId, role=$role")
            MockDelayService.delay()
            val user =
                MockUserDataSource.getUser(userId) ?: UserProfile.createObject(userId = userId)
            val updatedUser =
                user.copy(roles = user.roles.filterNot { it.role == role && it.schoolId == schoolId })
            MockUserDataSource.updateUser(updatedUser)
            logger?.i(tag, "removeRole_success", "Role $role removed from user $userId")
            Unit
        }
}