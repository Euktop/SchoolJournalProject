// data/repository/AdminRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.MockData
import stud.euktop.domain.model.*
import stud.euktop.domain.repository.AdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepositoryImpl @Inject constructor() : AdminRepository {

    // ==================== ПОЛЬЗОВАТЕЛИ ====================

    override suspend fun getUsers(): Result<List<UserInfo>> {
        MockData.delay()
        val usersWithRoles = MockData.users.map { user ->
            val roles = MockData.usersRoles
                .filter { it.first == user.userId }
                .map { RoleSchools(it.second, it.third) }
            user.copy(roles = roles)
        }
        return Result.success(usersWithRoles)
    }

    override suspend fun getUser(userId: Int): Result<UserInfo> {
        MockData.delay()
        val user = MockData.users.find { it.userId == userId }
        return if (user != null) {
            val roles = MockData.usersRoles
                .filter { it.first == userId }
                .map { RoleSchools(it.second, it.third) }
            Result.success(user.copy(roles = roles))
        } else {
            Result.failure(NoSuchElementException("User not found"))
        }
    }

    override suspend fun addUser(user: UserInfo, password: String?): Result<UserInfo> {
        MockData.delay()
        val newUserId = (MockData.users.maxOfOrNull { it.userId } ?: 0) + 1
        val newUser = user.copy(userId = newUserId)
        MockData.users.add(newUser)
        user.roles.forEach { roleSchool ->
            MockData.usersRoles.add(Triple(newUserId, roleSchool.role, roleSchool.schoolId))
        }
        return Result.success(newUser)
    }

    override suspend fun updateUser(user: UserInfo): Result<UserInfo> {
        MockData.delay()
        val index = MockData.users.indexOfFirst { it.userId == user.userId }
        if (index == -1) return Result.failure(NoSuchElementException("User not found"))
        MockData.users[index] = user
        MockData.usersRoles.removeAll { it.first == user.userId }
        user.roles.forEach { roleSchool ->
            MockData.usersRoles.add(Triple(user.userId, roleSchool.role, roleSchool.schoolId))
        }
        return Result.success(user)
    }

    override suspend fun deleteUser(userId: Int): Result<Unit> {
        MockData.delay()
        val removed = MockData.users.removeIf { it.userId == userId }
        if (removed) {
            MockData.usersRoles.removeAll { it.first == userId }
            return Result.success(Unit)
        }
        return Result.failure(NoSuchElementException("User not found"))
    }

    // ==================== ШКОЛЫ ====================

    override suspend fun getSchools(): Result<List<School>> {
        MockData.delay()
        return Result.success(MockData.schools.toList())
    }

    // ==================== УЧИТЕЛЯ (по роли) ====================

    override suspend fun getTeachersByRole(role: Role): Result<List<UserInfo>> {
        MockData.delay()
        // Получаем всех пользователей с нужной ролью
        val teacherIds = MockData.usersRoles
            .filter { it.second == role }
            .map { it.first }
            .distinct()
        val teachers = teacherIds.mapNotNull { userId ->
            MockData.users.find { it.userId == userId }?.let { user ->
                val roles = MockData.usersRoles
                    .filter { it.first == userId }
                    .map { RoleSchools(it.second, it.third) }
                user.copy(roles = roles)
            }
        }
        return Result.success(teachers)
    }

    // ==================== ПРЕДМЕТЫ (будут позже) ====================

    override suspend fun getSubjects(): Result<List<Subject>> {
        MockData.delay()
        return Result.success(MockData.subjectInfos)
    }

    // ==================== НАЗНАЧЕНИЯ УЧИТЕЛЕЙ (будут позже) ====================

    override suspend fun getTeacherAssignments(): Result<List<TeacherAssignment>> {
        MockData.delay()
        return Result.success(MockData.assignmentInfos)
    }

    override suspend fun getClasses(): Result<List<ClassInfo>> {
        MockData.delay()
        return Result.success(MockData.classInfos.toList())
    }

    override suspend fun getClass(classId: Int): Result<ClassInfo> {
        MockData.delay()
        val classInfo = MockData.classInfos.find { it.classId == classId }
        return if (classInfo != null) Result.success(classInfo)
        else Result.failure(NoSuchElementException("Class not found"))
    }

    override suspend fun addClass(classInfo: ClassInfo): Result<ClassInfo> {
        MockData.delay()
        val newId = (MockData.classInfos.maxOfOrNull { it.classId } ?: 0) + 1
        val newClass = classInfo.copy(classId = newId)
        MockData.classInfos.add(newClass)
        return Result.success(newClass)
    }

    override suspend fun updateClass(classInfo: ClassInfo): Result<ClassInfo> {
        MockData.delay()
        val index = MockData.classInfos.indexOfFirst { it.classId == classInfo.classId }
        if (index == -1) return Result.failure(NoSuchElementException("Class not found"))
        MockData.classInfos[index] = classInfo
        return Result.success(classInfo)
    }

    override suspend fun deleteClass(classId: Int): Result<Unit> {
        MockData.delay()
        val removed = MockData.classInfos.removeIf { it.classId == classId }
        return if (removed) Result.success(Unit)
        else Result.failure(NoSuchElementException("Class not found"))
    }

    // data/repository/AdminRepositoryImpl.kt
    override suspend fun getSubject(subjectId: Int): Result<Subject> {
        MockData.delay()
        val subject = MockData.subjects.find { it.subjectId == subjectId }
        return if (subject != null) Result.success(subject)
        else Result.failure(NoSuchElementException("Subject not found"))
    }

    override suspend fun addSubject(subject: Subject): Result<Subject> {
        MockData.delay()
        val newId = (MockData.subjects.maxOfOrNull { it.subjectId } ?: 0) + 1
        val newSubject = subject.copy(subjectId = newId)
        MockData.subjects.add(newSubject)
        return Result.success(newSubject)
    }

    override suspend fun updateSubject(subject: Subject): Result<Subject> {
        MockData.delay()
        val index = MockData.subjects.indexOfFirst { it.subjectId == subject.subjectId }
        if (index == -1) return Result.failure(NoSuchElementException("Subject not found"))
        MockData.subjects[index] = subject
        return Result.success(subject)
    }

    override suspend fun deleteSubject(subjectId: Int): Result<Unit> {
        MockData.delay()
        val removed = MockData.subjects.removeIf { it.subjectId == subjectId }
        return if (removed) Result.success(Unit)
        else Result.failure(NoSuchElementException("Subject not found"))
    }
}