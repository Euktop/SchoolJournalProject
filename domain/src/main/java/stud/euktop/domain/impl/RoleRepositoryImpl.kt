package stud.euktop.domain.impl

import stud.euktop.domain.contract.RoleRepository
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.common.DataError
import stud.euktop.domain.model.user.UserRole
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoleRepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository
) : RoleRepository {
    // Опционально, можно будет в network сделать, чтобы он делал реальный запрос только раз в 30 минут или пока клиент четко не запросит провести проверку
    private var cachedProfile: UserProfile? = null
    private suspend fun getProfile(): UserProfile {
        if (cachedProfile == null) {
            val result = authRepository.getCurrentUser()
            cachedProfile = result.getOrNull()
        }
        return cachedProfile ?: throw DataError.Unknown(null)
    }

    override suspend fun getAvailableRoles(): List<Role> {
        val roles = getRoles().map { it.role }

        val hasAdmin = Role.ADMIN in roles
        val hasDirector = Role.DIRECTOR in roles

        return Role.entries.filter { role ->
            when (role) {
                Role.ADMIN,
                Role.DIRECTOR -> hasAdmin
                Role.TEACHER,
                Role.STUDENT,
                Role.PARENT -> hasAdmin || hasDirector
            }
        }
    }

    override suspend fun getRoles(): List<UserRole> = getProfile().roles

    override suspend fun hasRole(role: Role): Boolean {
        return getProfile().roles.any { it.role == role }
    }

    override suspend fun hasRole(role: UserRole): Boolean {
        return getProfile().roles.contains(role)
    }

    override suspend fun isAdmin(): Boolean = hasRole(Role.ADMIN)
    override suspend fun isDirector(): Boolean = hasRole(Role.DIRECTOR)
    override suspend fun isTeacher(): Boolean = hasRole(Role.TEACHER)
    override suspend fun isStudent(): Boolean = hasRole(Role.STUDENT)
    override suspend fun isParent(): Boolean = hasRole(Role.PARENT)

    override suspend fun getCurrentSchoolId(): Int? {
        val roles = getProfile().roles
        return roles.firstNotNullOfOrNull { it.schoolId }
    }

    override suspend fun canEditUsers(): Boolean = isAdmin() || isDirector()
    override suspend fun canEditClasses(): Boolean = isAdmin() || isDirector()
    override suspend fun canManageAssignments(): Boolean = isAdmin() || isDirector()
    override suspend fun canViewAdminPanel(): Boolean = isAdmin() || isDirector()
    override suspend fun canEditUser(userId: Int): Boolean =
        canEditUsers() || getProfile().userId == userId
}