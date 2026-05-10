package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockUserDataSource
import stud.euktop.data.mock.data.fullName
import stud.euktop.domain.model.user.*
import stud.euktop.domain.repository.UserAdminRepository
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAdminMockRepositoryImpl @Inject constructor() : UserAdminRepository {
    override suspend fun getUsers(filter: UserFilter): Result<List<UserListItem>> {
        MockDelayService.delay()
        val users = MockUserDataSource.getAllUsersWithRoles()
        val filtered = users.filter { user ->
            (filter.fullName == null || user.fullName().contains(filter.fullName!!, ignoreCase = true)) &&
                    (filter.role == null || user.roles.any { it.role == filter.role }) &&
                    (filter.accountStatus == null || user.accountStatus == filter.accountStatus)
        }.map {
            UserListItem(it.userId, it.lastName, it.firstName, it.surName, it.email, it.accountStatus)
        }
        val paged = filtered.drop(filter.pagination.offset ?: 0).take(filter.pagination.limit ?: Int.MAX_VALUE)
        return Result.success(paged)
    }

    override suspend fun getUser(userId: Int): Result<UserProfile> {
        MockDelayService.delay()
        val user = MockUserDataSource.getUser(userId) ?: return Result.failure(NoSuchElementException())
        return Result.success(user)
    }

    override suspend fun addUser(profile: UserProfile, password: String?): Result<UserProfile> {
        MockDelayService.delay()
        val newUser = profile.copy(userId = 0, dateRegistration = Date())
        val created = MockUserDataSource.addUser(newUser, profile.roles)
        return Result.success(created)
    }

    override suspend fun updateUser(update: UserUpdate): Result<UserProfile> {
        MockDelayService.delay()
        val existing = MockUserDataSource.getUser(update.userId)
            ?: return Result.failure(NoSuchElementException())
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
        return Result.success(updated)
    }

    override suspend fun deleteUser(userId: Int): Result<Unit> {
        MockDelayService.delay()
        return if (MockUserDataSource.deleteUser(userId)) Result.success(Unit)
        else Result.failure(NoSuchElementException())
    }

    override suspend fun addUserRole(userId: Int, role: Role, schoolId: Int?): Result<UserRole> {
        MockDelayService.delay()
        val user = MockUserDataSource.getUser(userId) ?: return Result.failure(NoSuchElementException())
        val newRole = UserRole(role, schoolId, Date())
        val updatedUser = user.copy(roles = user.roles + newRole)
        MockUserDataSource.updateUser(updatedUser)
        return Result.success(newRole)
    }

    override suspend fun removeRole(userId: Int, role: Role, schoolId: Int?): Result<Unit> {
        MockDelayService.delay()
        val user = MockUserDataSource.getUser(userId) ?: return Result.failure(NoSuchElementException())
        val updatedUser = user.copy(roles = user.roles.filterNot { it.role == role && it.schoolId == schoolId })
        MockUserDataSource.updateUser(updatedUser)
        return Result.success(Unit)
    }
}