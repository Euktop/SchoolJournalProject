// UserAdminRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockUserDataSource
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.model.user.UserInfoFilter
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAdminRepositoryImpl @Inject constructor() : UserAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getUsers(filter: UserInfoFilter): Result<List<UserInfo>> {
        logger?.i(tag, "getUsers started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val users = MockUserDataSource.getAllUsersWithRoles()
            logger?.i(tag, "getUsers succeeded", "count=${users.size}")
            Result.success(users)
        } catch (e: Exception) {
            logger?.e(tag, "getUsers failed", e, "filter=$filter")
            Result.failure(e)
        }
    }

    override suspend fun getUser(userId: Int): Result<UserInfo> {
        logger?.i(tag, "getUser started", "userId=$userId")
        MockDelayService.delay()
        return try {
            val user = MockUserDataSource.getUser(userId)
            if (user != null) {
                logger?.i(tag, "getUser succeeded", "userId=$userId")
                Result.success(user)
            } else {
                val ex = NoSuchElementException("User not found")
                logger?.e(tag, "getUser failed", ex, "userId=$userId not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "getUser exception", e, "userId=$userId")
            Result.failure(e)
        }
    }

    override suspend fun addUser(user: UserInfo, password: String?): Result<UserInfo> {
        logger?.i(tag, "addUser started", "user=$user, password=${if (password != null) "***" else null}")
        MockDelayService.delay()
        return try {
            val newUser = MockUserDataSource.addUser(user, user.roles)
            logger?.i(tag, "addUser succeeded", "newId=${newUser.userId}")
            Result.success(newUser)
        } catch (e: Exception) {
            logger?.e(tag, "addUser failed", e, "user=$user")
            Result.failure(e)
        }
    }

    override suspend fun updateUser(user: UserInfo): Result<UserInfo> {
        logger?.i(tag, "updateUser started", "user=$user")
        MockDelayService.delay()
        return try {
            MockUserDataSource.updateUser(user)
            logger?.i(tag, "updateUser succeeded", "userId=${user.userId}")
            Result.success(user)
        } catch (e: Exception) {
            logger?.e(tag, "updateUser failed", e, "user=$user")
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(userId: Int): Result<Unit> {
        logger?.i(tag, "deleteUser started", "userId=$userId")
        MockDelayService.delay()
        return try {
            val deleted = MockUserDataSource.deleteUser(userId)
            if (deleted) {
                logger?.i(tag, "deleteUser succeeded", "userId=$userId")
                Result.success(Unit)
            } else {
                val ex = NoSuchElementException("User not found")
                logger?.e(tag, "deleteUser failed", ex, "userId=$userId not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "deleteUser exception", e, "userId=$userId")
            Result.failure(e)
        }
    }

    override suspend fun getTeachersByRole(role: Role): Result<List<UserInfo>> {
        logger?.i(tag, "getTeachersByRole started", "role=$role")
        MockDelayService.delay()
        return try {
            val teachers = MockUserDataSource.getTeachersByRole(role)
            logger?.i(tag, "getTeachersByRole succeeded", "role=$role, count=${teachers.size}")
            Result.success(teachers)
        } catch (e: Exception) {
            logger?.e(tag, "getTeachersByRole failed", e, "role=$role")
            Result.failure(e)
        }
    }
}