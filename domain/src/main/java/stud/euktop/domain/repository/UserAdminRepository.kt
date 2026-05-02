package stud.euktop.domain.repository

import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.RoleSchools
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.model.user.UserInfoFilter

interface UserAdminRepository {
    suspend fun getUsers(filter: UserInfoFilter = UserInfoFilter()): Result<List<UserInfo>>
    suspend fun getUser(userId: Int): Result<UserInfo>
    suspend fun addUser(user: UserInfo, password: String?): Result<UserInfo>
    suspend fun updateUser(user: UserInfo): Result<UserInfo>
    suspend fun deleteUser(userId: Int): Result<Unit>
    suspend fun getTeachersByRole(role: Role): Result<List<UserInfo>>
}