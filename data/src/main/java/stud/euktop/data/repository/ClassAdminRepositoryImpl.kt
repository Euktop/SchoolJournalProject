package stud.euktop.data.repository

import stud.euktop.data.mock.MockClassDataSource
import stud.euktop.data.mock.MockDelayService
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.repository.ClassAdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassAdminRepositoryImpl @Inject constructor() : ClassAdminRepository {
    override suspend fun getClasses(): Result<List<ClassInfo>> {
        MockDelayService.delay()
        return Result.success(MockClassDataSource.getAll())
    }

    override suspend fun getClass(classId: Int): Result<ClassInfo> {
        MockDelayService.delay()
        return MockClassDataSource.get(classId)?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("Class not found"))
    }

    override suspend fun addClass(classInfo: ClassInfo): Result<ClassInfo> {
        MockDelayService.delay()
        return Result.success(MockClassDataSource.add(classInfo))
    }

    override suspend fun updateClass(classInfo: ClassInfo): Result<ClassInfo> {
        MockDelayService.delay()
        MockClassDataSource.update(classInfo)
        return Result.success(classInfo)
    }

    override suspend fun deleteClass(classId: Int): Result<Unit> {
        MockDelayService.delay()
        return if (MockClassDataSource.delete(classId)) Result.success(Unit)
        else Result.failure(NoSuchElementException("Class not found"))
    }
}