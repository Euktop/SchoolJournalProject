package stud.euktop.domain.repository

import stud.euktop.domain.model.school.ClassInfo

interface ClassAdminRepository {
    suspend fun getClasses(): Result<List<ClassInfo>>
    suspend fun getClass(classId: Int): Result<ClassInfo>
    suspend fun addClass(classInfo: ClassInfo): Result<ClassInfo>
    suspend fun updateClass(classInfo: ClassInfo): Result<ClassInfo>
    suspend fun deleteClass(classId: Int): Result<Unit>
}
