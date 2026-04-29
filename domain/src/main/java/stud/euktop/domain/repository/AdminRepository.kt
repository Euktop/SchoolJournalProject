package stud.euktop.domain.repository

import stud.euktop.domain.model.*

interface AdminRepository {
    suspend fun getUsers(): Result<List<UserInfo>>
    suspend fun getClasses(): Result<List<ClassInfo>>
    suspend fun getSubjects(): Result<List<Subject>>
    suspend fun getTeacherAssignments(): Result<List<TeacherAssignment>>
}