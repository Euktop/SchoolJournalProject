// domain/repository/AdminRepository.kt
package stud.euktop.domain.repository

import stud.euktop.domain.model.*

interface AdminRepository {
    suspend fun getUsers(): Result<List<UserInfo>>
    suspend fun getClasses(): Result<List<ClassInfo>>
    suspend fun getSubjects(): Result<List<Subject>>
    suspend fun getTeacherAssignments(): Result<List<TeacherAssignment>>

    suspend fun getUser(userId: Int): Result<UserInfo>
    suspend fun addUser(user: UserInfo, password: String?): Result<UserInfo>
    suspend fun updateUser(user: UserInfo): Result<UserInfo>
    suspend fun deleteUser(userId: Int): Result<Unit>

    suspend fun getSchools(): Result<List<School>>

    suspend fun getClass(classId: Int): Result<ClassInfo>
    suspend fun addClass(classInfo: ClassInfo): Result<ClassInfo>
    suspend fun updateClass(classInfo: ClassInfo): Result<ClassInfo>
    suspend fun deleteClass(classId: Int): Result<Unit>

    suspend fun getTeachersByRole(role: Role): Result<List<UserInfo>>
    suspend fun getSubject(subjectId: Int): Result<Subject>
    suspend fun addSubject(subject: Subject): Result<Subject>
    suspend fun updateSubject(subject: Subject): Result<Subject>
    suspend fun deleteSubject(subjectId: Int): Result<Unit>

}