package stud.euktop.data.repository

import stud.euktop.data.MockData
import stud.euktop.domain.model.*
import stud.euktop.domain.repository.AdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepositoryImpl @Inject constructor() : AdminRepository {
    override suspend fun getUsers(): Result<List<UserInfo>> =
        Result.success(MockData.adminUsers)
    override suspend fun getClasses(): Result<List<ClassInfo>> =
        Result.success(MockData.adminClasses)
    override suspend fun getSubjects(): Result<List<Subject>> =
        Result.success(MockData.adminSubjects)
    override suspend fun getTeacherAssignments(): Result<List<TeacherAssignment>> =
        Result.success(MockData.adminAssignments)
}
