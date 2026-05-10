package stud.euktop.domain.repository

import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.ClassInfoFilter
import stud.euktop.domain.model.school.ClassInfoUpdate

interface ClassAdminRepository {
    suspend fun getClasses(filter: ClassInfoFilter = ClassInfoFilter()): Result<List<ClassInfo>>
    suspend fun updateClass(update: ClassInfoUpdate): Result<ClassInfo>
    suspend fun getClass(classId: Int): Result<ClassInfo>
    suspend fun addClass(classInfo: ClassInfo): Result<ClassInfo>
    suspend fun deleteClass(classId: Int): Result<Unit>
}