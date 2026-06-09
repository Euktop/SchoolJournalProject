package stud.euktop.domain.contract

import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserRole

interface RoleRepository {
    suspend fun getAvailableRoles(): List<Role>
    suspend fun getRoles(): List<UserRole>
    suspend fun hasRole(role: Role): Boolean
    suspend fun hasRole(role: UserRole): Boolean
    suspend fun isAdmin(): Boolean
    suspend fun isDirector(): Boolean
    suspend fun isTeacher(): Boolean
    suspend fun isStudent(): Boolean
    suspend fun isParent(): Boolean
    suspend fun getCurrentSchoolId(): Int?
    suspend fun canEditUsers(): Boolean
    suspend fun canEditClasses(): Boolean
    suspend fun canManageAssignments(): Boolean
    suspend fun canViewAdminPanel(): Boolean

    suspend fun canEditUser(userId: Int): Boolean
    suspend fun getCurrentRole(): Role?
}