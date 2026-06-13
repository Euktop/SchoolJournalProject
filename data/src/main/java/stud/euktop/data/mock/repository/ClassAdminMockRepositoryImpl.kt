package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockClassDataSource
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.util.filterParam
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.ClassInfoFilter
import stud.euktop.domain.model.school.ClassInfoUpdate
import stud.euktop.domain.repository.ClassAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassAdminMockRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler
) : ClassAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getClasses(filter: ClassInfoFilter): Result<List<ClassInfo>> {
        logger?.i(tag, "getClasses started", "filter=$filter")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
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
            filtered.drop(filter.pagination.offset ?: 0)
                .take(filter.pagination.limit ?: Int.MAX_VALUE)
                .ifEmpty { MockClassDataSource.getAll().take(1) }
        }
    }

    override suspend fun getClass(classId: Int): Result<ClassInfo> {
        logger?.i(tag, "getClass started", "classId=$classId")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            MockClassDataSource.get(classId) ?: ClassInfo.createObject(
                classId = classId,
                schoolId = 0,
                grade = 0,
                letter = "А",
                academicYearStart = 2024,
                academicYearEnd = 2025,
                teacherId = null
            )
        }
    }

    override suspend fun addClass(classInfo: ClassInfo): Result<ClassInfo> {
        logger?.i(tag, "addClass started", "classInfo=$classInfo")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            MockClassDataSource.add(classInfo)
        }
    }

    override suspend fun updateClass(update: ClassInfoUpdate): Result<ClassInfo> {
        logger?.i(tag, "updateClass started", "update=$update")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val existing = MockClassDataSource.get(update.classId)
                ?: ClassInfo.createObject(update.classId, 0, 0, "А", 2024, 2025, null)
            val updated = existing.copy(
                schoolId = update.schoolId.uValue ?: existing.schoolId,
                grade = update.grade.uValue ?: existing.grade,
                letter = update.letter.uValue ?: existing.letter,
                academicYearStart = update.academicYearStart.uValue ?: existing.academicYearStart,
                academicYearEnd = update.academicYearEnd.uValue ?: existing.academicYearEnd,
                teacherId = update.teacherId.uValue ?: existing.teacherId
            )
            MockClassDataSource.update(updated)
            updated
        }
    }

    override suspend fun deleteClass(classId: Int): Result<Unit> {
        logger?.i(tag, "deleteClass started", "classId=$classId")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val deleted = MockClassDataSource.delete(classId)
            if (!deleted) {
                logger?.d(
                    tag,
                    "deleteClass_warning",
                    "Class not found, returning success for idempotency"
                )
            }
            Unit
        }
    }
}