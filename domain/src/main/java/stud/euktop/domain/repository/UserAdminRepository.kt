package stud.euktop.domain.repository

import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.model.user.UserRole
import stud.euktop.domain.model.user.UserUpdate

interface UserAdminRepository {
    suspend fun getUsers(filter: UserFilter = UserFilter()): Result<List<UserListItem>>
    suspend fun getUser(userId: Int): Result<UserProfile>
    suspend fun addUser(profile: UserProfile, password: String?): Result<UserProfile>
    suspend fun updateUser(update: UserUpdate): Result<UserProfile>
    suspend fun deleteUser(userId: Int): Result<Unit>
    suspend fun addUserRole(
        userId: Int,
        role: Role,
        schoolId: Int?
    ): Result<UserRole>
    suspend fun removeRole(
        userId: Int,
        role: Role,
        schoolId: Int?
    ): Result<Unit>
}