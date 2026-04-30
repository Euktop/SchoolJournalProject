package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockUserDataSource
import stud.euktop.domain.model.auth.Role
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.repository.UserAdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAdminRepositoryImpl @Inject constructor() : UserAdminRepository {
    override suspend fun getUsers(): Result<List<UserInfo>> {
        MockDelayService.delay()
        return Result.success(MockUserDataSource.getAllUsersWithRoles())
    }

    override suspend fun getUser(userId: Int): Result<UserInfo> {
        MockDelayService.delay()
        return MockUserDataSource.getUser(userId)?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("User not found"))
    }

    override suspend fun addUser(user: UserInfo, password: String?): Result<UserInfo> {
        MockDelayService.delay()
        val newUser = MockUserDataSource.addUser(user, user.roles)
        return Result.success(newUser)
    }

    override suspend fun updateUser(user: UserInfo): Result<UserInfo> {
        MockDelayService.delay()
        MockUserDataSource.updateUser(user)
        return Result.success(user)
    }

    override suspend fun deleteUser(userId: Int): Result<Unit> {
        MockDelayService.delay()
        return if (MockUserDataSource.deleteUser(userId)) Result.success(Unit)
        else Result.failure(NoSuchElementException("User not found"))
    }

    override suspend fun getTeachersByRole(role: Role): Result<List<UserInfo>> {
        MockDelayService.delay()
        return Result.success(MockUserDataSource.getTeachersByRole(role))
    }
}