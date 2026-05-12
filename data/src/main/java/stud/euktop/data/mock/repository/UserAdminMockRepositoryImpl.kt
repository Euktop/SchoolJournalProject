package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockUserDataSource
import stud.euktop.data.mock.data.fullName
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.model.user.UserRole
import stud.euktop.domain.model.user.UserUpdate
import stud.euktop.domain.repository.UserAdminRepository
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAdminMockRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler
) : UserAdminRepository {

    override suspend fun getUsers(filter: UserFilter): Result<List<UserListItem>> {
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val users = MockUserDataSource.getAllUsersWithRoles()
            val filtered = users.filter { user ->
                (filter.fullName == null || user.fullName().contains(filter.fullName!!, ignoreCase = true)) &&
                        (filter.role == null || user.roles.any { it.role == filter.role }) &&
                        (filter.accountStatus == null || user.accountStatus == filter.accountStatus)
            }.map {
                UserListItem(it.userId, it.lastName, it.firstName, it.surName, it.email, it.accountStatus)
            }
            filtered.drop(filter.pagination.offset ?: 0).take(filter.pagination.limit ?: Int.MAX_VALUE)
        }
    }

    override suspend fun getUser(userId: Int): Result<UserProfile> {
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            MockUserDataSource.getUser(userId) ?: throw NoSuchElementException()
        }
    }

    override suspend fun addUser(profile: UserProfile, password: String?): Result<UserProfile> {
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val newUser = profile.copy(userId = 0, dateRegistration = Date())
            MockUserDataSource.addUser(newUser, profile.roles)
        }
    }

    override suspend fun updateUser(update: UserUpdate): Result<UserProfile> {
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val existing = MockUserDataSource.getUser(update.userId) ?: throw NoSuchElementException()
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
            updated
        }
    }

    override suspend fun deleteUser(userId: Int): Result<Unit> {
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            if (!MockUserDataSource.deleteUser(userId)) throw NoSuchElementException()
        }
    }

    override suspend fun addUserRole(userId: Int, role: Role, schoolId: Int?): Result<UserRole> {
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val user = MockUserDataSource.getUser(userId) ?: throw NoSuchElementException()
            val newRole = UserRole(role, schoolId, Date())
            val updatedUser = user.copy(roles = user.roles + newRole)
            MockUserDataSource.updateUser(updatedUser)
            newRole
        }
    }

    override suspend fun removeRole(userId: Int, role: Role, schoolId: Int?): Result<Unit> {
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val user = MockUserDataSource.getUser(userId) ?: throw NoSuchElementException()
            val updatedUser = user.copy(roles = user.roles.filterNot { it.role == role && it.schoolId == schoolId })
            MockUserDataSource.updateUser(updatedUser)
        }
    }
}