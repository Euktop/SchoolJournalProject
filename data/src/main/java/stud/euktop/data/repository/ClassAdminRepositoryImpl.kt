// ClassAdminRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.mock.MockClassDataSource
import stud.euktop.data.mock.MockDelayService
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.repository.ClassAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassAdminRepositoryImpl @Inject constructor() : ClassAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getClasses(): Result<List<ClassInfo>> {
        logger?.i(tag, "getClasses started")
        MockDelayService.delay()
        return try {
            val classes = MockClassDataSource.getAll()
            logger?.i(tag, "getClasses succeeded", "count=${classes.size}")
            Result.success(classes)
        } catch (e: Exception) {
            logger?.e(tag, "getClasses failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getClass(classId: Int): Result<ClassInfo> {
        logger?.i(tag, "getClass started", "classId=$classId")
        MockDelayService.delay()
        return try {
            val classInfo = MockClassDataSource.get(classId)
            if (classInfo != null) {
                logger?.i(tag, "getClass succeeded", "classId=$classId")
                Result.success(classInfo)
            } else {
                val ex = NoSuchElementException("Class not found")
                logger?.e(tag, "getClass failed", ex, "classId=$classId not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "getClass exception", e, "classId=$classId")
            Result.failure(e)
        }
    }

    override suspend fun addClass(classInfo: ClassInfo): Result<ClassInfo> {
        logger?.i(tag, "addClass started", "classInfo=$classInfo")
        MockDelayService.delay()
        return try {
            val newClass = MockClassDataSource.add(classInfo)
            logger?.i(tag, "addClass succeeded", "newId=${newClass.classId}")
            Result.success(newClass)
        } catch (e: Exception) {
            logger?.e(tag, "addClass failed", e, "classInfo=$classInfo")
            Result.failure(e)
        }
    }

    override suspend fun updateClass(classInfo: ClassInfo): Result<ClassInfo> {
        logger?.i(tag, "updateClass started", "classInfo=$classInfo")
        MockDelayService.delay()
        return try {
            MockClassDataSource.update(classInfo)
            logger?.i(tag, "updateClass succeeded", "classId=${classInfo.classId}")
            Result.success(classInfo)
        } catch (e: Exception) {
            logger?.e(tag, "updateClass failed", e, "classInfo=$classInfo")
            Result.failure(e)
        }
    }

    override suspend fun deleteClass(classId: Int): Result<Unit> {
        logger?.i(tag, "deleteClass started", "classId=$classId")
        MockDelayService.delay()
        return try {
            val deleted = MockClassDataSource.delete(classId)
            if (deleted) {
                logger?.i(tag, "deleteClass succeeded", "classId=$classId")
                Result.success(Unit)
            } else {
                val ex = NoSuchElementException("Class not found")
                logger?.e(tag, "deleteClass failed", ex, "classId=$classId not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "deleteClass exception", e, "classId=$classId")
            Result.failure(e)
        }
    }
}