// LessonRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockLessonDataSource
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.lesson.LessonFilter
import stud.euktop.domain.model.lesson.LessonFilter2
import stud.euktop.domain.repository.LessonRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRepositoryImpl @Inject constructor() : LessonRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getLesson(lessonId: Int): Result<Lesson> {
        logger?.i(tag, "getLesson started", "lessonId=$lessonId")
        MockDelayService.delay()
        return try {
            val lesson = MockLessonDataSource.getLesson(lessonId)
            if (lesson != null) {
                logger?.i(tag, "getLesson succeeded", "lessonId=$lessonId")
                Result.success(lesson)
            } else {
                val ex = NoSuchElementException("Lesson not found")
                logger?.e(tag, "getLesson failed", ex, "lessonId=$lessonId not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "getLesson exception", e, "lessonId=$lessonId")
            Result.failure(e)
        }
    }

    override suspend fun getLessons(filter: LessonFilter): Result<List<Lesson>> {
        logger?.i(tag, "getLessons (LessonFilter) started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val all = MockLessonDataSource.getAll()
            val filtered = all.filter { lesson ->
                (filter.classInfo?.classId == null || lesson.classInfo.classId == filter.classInfo?.classId)
            }
            logger?.i(tag, "getLessons (LessonFilter) succeeded", "count=${filtered.size}")
            Result.success(filtered)
        } catch (e: Exception) {
            logger?.e(tag, "getLessons (LessonFilter) failed", e, "filter=$filter")
            Result.failure(e)
        }
    }

    override suspend fun getLessons(filter: LessonFilter2): Result<List<Lesson>> {
        logger?.i(tag, "getLessons (LessonFilter2) started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val all = MockLessonDataSource.getAll()
            val filtered = all.filter { lesson ->
                (filter.classId == null || lesson.classInfo.classId == filter.classId)
            }
            logger?.i(tag, "getLessons (LessonFilter2) succeeded", "count=${filtered.size}")
            Result.success(filtered)
        } catch (e: Exception) {
            logger?.e(tag, "getLessons (LessonFilter2) failed", e, "filter=$filter")
            Result.failure(e)
        }
    }

    override suspend fun addLesson(lesson: Lesson): Result<Lesson> {
        logger?.i(tag, "addLesson started", "lesson=$lesson")
        MockDelayService.delay()
        return try {
            val newLesson = MockLessonDataSource.addLesson(lesson)
            logger?.i(tag, "addLesson succeeded", "newId=${newLesson.lessonId}")
            Result.success(newLesson)
        } catch (e: Exception) {
            logger?.e(tag, "addLesson failed", e, "lesson=$lesson")
            Result.failure(e)
        }
    }

    override suspend fun updateLesson(lesson: Lesson): Result<Lesson> {
        logger?.i(tag, "updateLesson started", "lesson=$lesson")
        MockDelayService.delay()
        return try {
            MockLessonDataSource.updateLesson(lesson)
            logger?.i(tag, "updateLesson succeeded", "lessonId=${lesson.lessonId}")
            Result.success(lesson)
        } catch (e: Exception) {
            logger?.e(tag, "updateLesson failed", e, "lesson=$lesson")
            Result.failure(e)
        }
    }

    override suspend fun deleteLesson(lessonId: Int): Result<Unit> {
        logger?.i(tag, "deleteLesson started", "lessonId=$lessonId")
        MockDelayService.delay()
        return try {
            val deleted = MockLessonDataSource.deleteLesson(lessonId)
            if (deleted) {
                logger?.i(tag, "deleteLesson succeeded", "lessonId=$lessonId")
                Result.success(Unit)
            } else {
                val ex = NoSuchElementException("Lesson not found")
                logger?.e(tag, "deleteLesson failed", ex, "lessonId=$lessonId not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "deleteLesson exception", e, "lessonId=$lessonId")
            Result.failure(e)
        }
    }
}