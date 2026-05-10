// data/src/main/java/stud/euktop/data/mock/repository/ClassAdminMockRepositoryImpl.kt
package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockClassDataSource
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.util.filterParam
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.ClassInfoFilter
import stud.euktop.domain.model.school.ClassInfoUpdate
import stud.euktop.domain.repository.ClassAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassAdminMockRepositoryImpl @Inject constructor() : ClassAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getClasses(filter: ClassInfoFilter): Result<List<ClassInfo>> {
        logger?.i(tag, "getClasses started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val all = MockClassDataSource.getAll()
            val filtered = all.filter { clazz ->
                filterParam(
                    filter.schoolId to clazz.schoolId,
                    filter.teacherId to clazz.teacherId,
                    filter.academicYearStart to clazz.academicYearStart
                ) && (filter.query == null || "${clazz.grade}${clazz.letter}".contains(
                    filter.query!!,
                    ignoreCase = true
                ))
            }
            val paged = filtered.drop(filter.pagination.offset ?: 0)
                .take(filter.pagination.limit ?: Int.MAX_VALUE)
            Result.success(paged)
        } catch (e: Exception) {
            logger?.e(tag, "getClasses failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getClass(classId: Int): Result<ClassInfo> {
        logger?.i(tag, "getClass started", "classId=$classId")
        MockDelayService.delay()
        return try {
            val clazz = MockClassDataSource.get(classId)
            if (clazz != null) {
                logger?.i(tag, "getClass succeeded", "classId=$classId")
                Result.success(clazz)
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

    override suspend fun updateClass(update: ClassInfoUpdate): Result<ClassInfo> {
        logger?.i(tag, "updateClass started", "update=$update")
        MockDelayService.delay()
        return try {
            val existing = MockClassDataSource.get(update.classId)
                ?: return Result.failure(NoSuchElementException("Class not found"))

            val updated = existing.copy(
                schoolId = update.schoolId.uValue ?: existing.schoolId,
                grade = update.grade.uValue ?: existing.grade,
                letter = update.letter.uValue ?: existing.letter,
                academicYearStart = update.academicYearStart.uValue ?: existing.academicYearStart,
                academicYearEnd = update.academicYearEnd.uValue ?: existing.academicYearEnd,
                teacherId = update.teacherId.uValue ?: existing.teacherId
            )
            MockClassDataSource.update(updated)
            logger?.i(tag, "updateClass succeeded", "classId=${update.classId}")
            Result.success(updated)
        } catch (e: Exception) {
            logger?.e(tag, "updateClass failed", e, "update=$update")
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