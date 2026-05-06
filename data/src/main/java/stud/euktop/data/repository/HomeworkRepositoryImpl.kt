// HomeworkRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockHomeworkDataSource
import stud.euktop.domain.model.common.DataError
import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.homework.HomeworkFilter
import stud.euktop.domain.repository.HomeworkRepository
import stud.euktop.data.mock.filterParam
import stud.euktop.domain.model.homework.HomeworkFilter2
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeworkRepositoryImpl @Inject constructor() : HomeworkRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getHomeworks(filter: HomeworkFilter): Result<List<Homework>> {
        logger?.i(tag, "getHomeworks (HomeworkFilter) started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val filtered = MockHomeworkDataSource.getAll().filter { h ->
                filterParam(
                    filter.lesson?.lessonId to h.lesson.lessonId,
                    filter.createdByUser to h.createdByUser.userId,
                ) && (filter.createdDiff.dateStart == null || filter.createdDiff.dateStart!! <= h.createdAt) && (filter.createdDiff.dateEnd == null || filter.createdDiff.dateEnd!! >= h.createdAt) && (let {
                    val classIdFromFilter = filter.lessonFilter.classInfo?.classId
                    if (classIdFromFilter != null) {
                        h.lesson.classInfo.classId == classIdFromFilter
                    } else {
                        true
                    }
                })
            }
            logger?.i(tag, "getHomeworks (HomeworkFilter) succeeded", "count=${filtered.size}")
            Result.success(filtered)
        } catch (e: Exception) {
            logger?.e(tag, "getHomeworks (HomeworkFilter) failed", e, "filter=$filter")
            Result.failure(e)
        }
    }

    override suspend fun getHomeworks(filter: HomeworkFilter2): Result<List<Homework>> {
        logger?.i(tag, "getHomeworks (HomeworkFilter2) started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val all = MockHomeworkDataSource.getAll()
            val filtered = all.filter { homework ->
                (filter.homeworkId == null || homework.homeworkId == filter.homeworkId) &&
                        (filter.lessonId == null || homework.lesson.lessonId == filter.lessonId) &&
                        (filter.teacherId == null || homework.createdByUser.userId == filter.teacherId) &&
                        (filter.classId == null || homework.lesson.classInfo.classId == filter.classId) &&
                        (filter.subjectId == null || homework.lesson.subject.subjectId == filter.subjectId) &&
                        (filter.description == null || homework.description.contains(
                            filter.description!!,
                            ignoreCase = true
                        )) &&
                        (filter.createdFrom == null || homework.createdAt >= filter.createdFrom) &&
                        (filter.createdTo == null || homework.createdAt <= filter.createdTo)
            }
            logger?.i(tag, "getHomeworks (HomeworkFilter2) succeeded", "count=${filtered.size}")
            Result.success(filtered)
        } catch (e: Exception) {
            logger?.e(tag, "getHomeworks (HomeworkFilter2) failed", e, "filter=$filter")
            Result.failure(e)
        }
    }

    override suspend fun getHomeworkById(id: Int): Result<Homework> {
        logger?.i(tag, "getHomeworkById started", "id=$id")
        MockDelayService.delay()
        return try {
            val result = MockHomeworkDataSource.getAll().firstOrNull { it.homeworkId == id }
            if (result != null) {
                logger?.i(tag, "getHomeworkById succeeded", "id=$id")
                Result.success(result)
            } else {
                val ex = DataError.Unknown(null)
                logger?.e(tag, "getHomeworkById failed", ex, "id=$id not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "getHomeworkById exception", e, "id=$id")
            Result.failure(e)
        }
    }

    override suspend fun addHomework(homework: Homework): Result<Homework> {
        logger?.i(tag, "addHomework started", "homework=$homework")
        MockDelayService.delay()
        return try {
            val newHomework = MockHomeworkDataSource.add(homework)
            logger?.i(tag, "addHomework succeeded", "newId=${newHomework.homeworkId}")
            Result.success(newHomework)
        } catch (e: Exception) {
            logger?.e(tag, "addHomework failed", e, "homework=$homework")
            Result.failure(e)
        }
    }

    override suspend fun updateHomework(homework: Homework): Result<Homework> {
        logger?.i(tag, "updateHomework started", "homework=$homework")
        MockDelayService.delay()
        return try {
            MockHomeworkDataSource.update(homework)
            logger?.i(tag, "updateHomework succeeded", "homeworkId=${homework.homeworkId}")
            Result.success(homework)
        } catch (e: Exception) {
            logger?.e(tag, "updateHomework failed", e, "homework=$homework")
            Result.failure(e)
        }
    }

    override suspend fun deleteHomework(homeworkId: Int): Result<Unit> {
        logger?.i(tag, "deleteHomework started", "homeworkId=$homeworkId")
        MockDelayService.delay()
        return try {
            val deleted = MockHomeworkDataSource.delete(homeworkId)
            if (deleted) {
                logger?.i(tag, "deleteHomework succeeded", "homeworkId=$homeworkId")
                Result.success(Unit)
            } else {
                val ex = NoSuchElementException("Homework not found")
                logger?.e(tag, "deleteHomework failed", ex, "homeworkId=$homeworkId not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "deleteHomework exception", e, "homeworkId=$homeworkId")
            Result.failure(e)
        }
    }
}