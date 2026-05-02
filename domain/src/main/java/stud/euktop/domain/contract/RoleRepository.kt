package stud.euktop.domain.contract

import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.RoleSchools

interface RoleRepository {
    suspend fun getAvailableRoles(): List<Role>
    suspend fun getRoles(): List<RoleSchools>
    suspend fun hasRole(role: Role): Boolean
    suspend fun hasRole(role: RoleSchools): Boolean
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
}