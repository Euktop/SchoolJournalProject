// data/src/main/java/stud/euktop/data/mock/repository/LessonMockRepositoryImpl.kt
package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockClassDataSource
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockLessonDataSource
import stud.euktop.data.mock.data.MockRoomDataSource
import stud.euktop.data.mock.data.MockSubjectDataSource
import stud.euktop.data.mock.data.MockUserDataSource
import stud.euktop.data.mock.util.filterParam
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.lesson.LessonFilter
import stud.euktop.domain.model.lesson.LessonFull
import stud.euktop.domain.model.lesson.LessonUpdate
import stud.euktop.domain.model.user.UserRef
import stud.euktop.domain.repository.LessonRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonMockRepositoryImpl @Inject constructor() : LessonRepository {
    private val tag = this.toSimpleTag()

    private suspend fun enrichToFull(lesson: Lesson): LessonFull {
        val classInfo = MockClassDataSource.get(lesson.classId)
            ?: throw NoSuchElementException("Class not found for lesson ${lesson.lessonId}")
        val subject = MockSubjectDataSource.get(lesson.subjectId)
            ?: throw NoSuchElementException("Subject not found for lesson ${lesson.lessonId}")
        val teacher = MockUserDataSource.getUser(lesson.teacherId)
            ?: throw NoSuchElementException("Teacher not found for lesson ${lesson.lessonId}")
        val room = lesson.roomId?.let { MockRoomDataSource.get(it) }
        return LessonFull(
            lessonId = lesson.lessonId,
            classInfo = classInfo,
            subject = subject,
            teacher = UserRef(teacher.userId, teacher.lastName, teacher.firstName, teacher.surName),
            date = lesson.date,
            topic = lesson.topic ?: "",
            startTime = lesson.startTime,
            endTime = lesson.endTime,
            room = room,
            locationAddress = lesson.locationAddress
        )
    }

    override suspend fun getLesson(lessonId: Int): Result<LessonFull> {
        logger?.i(tag, "getLesson started", "lessonId=$lessonId")
        MockDelayService.delay()
        return try {
            val lesson = MockLessonDataSource.getLesson(lessonId)
            if (lesson != null) {
                val full = enrichToFull(lesson)
                Result.success(full)
            } else {
                Result.failure(NoSuchElementException("Lesson not found"))
            }
        } catch (e: Exception) {
            logger?.e(tag, "getLesson failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getLessons(filter: LessonFilter): Result<List<LessonFull>> {
        logger?.i(tag, "getLessons started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val all = MockLessonDataSource.getAll()
            val filtered = all.filter { lesson ->
                filterParam(
                    filter.lessonId to lesson.lessonId,
                    filter.classId to lesson.classId,
                    filter.subjectId to lesson.subjectId,
                    filter.teacherId to lesson.teacherId,
                    filter.roomId to lesson.roomId
                ) && (filter.dateFrom == null || lesson.date >= filter.dateFrom) &&
                        (filter.dateTo == null || lesson.date <= filter.dateTo)
            }
            val paged = filtered.drop(filter.pagination.offset ?: 0)
                .take(filter.pagination.limit ?: Int.MAX_VALUE)
            val fullList = paged.map { enrichToFull(it) }
            Result.success(fullList)
        } catch (e: Exception) {
            logger?.e(tag, "getLessons failed", e)
            Result.failure(e)
        }
    }

    override suspend fun addLesson(lesson: Lesson): Result<LessonFull> {
        logger?.i(tag, "addLesson started", "lesson=$lesson")
        MockDelayService.delay()
        return try {
            val newLesson = MockLessonDataSource.addLesson(lesson)
            val full = enrichToFull(newLesson)
            Result.success(full)
        } catch (e: Exception) {
            logger?.e(tag, "addLesson failed", e)
            Result.failure(e)
        }
    }

    override suspend fun updateLesson(update: LessonUpdate): Result<Lesson> {
        logger?.i(tag, "updateLesson started", "update=$update")
        MockDelayService.delay()
        return try {
            val existing = MockLessonDataSource.getLesson(update.lessonId)
                ?: return Result.failure(NoSuchElementException("Lesson not found"))
            val updated = existing.copy(
                classId = update.classId.uValue ?: existing.classId,
                subjectId = update.subjectId.uValue ?: existing.subjectId,
                teacherId = update.teacherId.uValue ?: existing.teacherId,
                date = update.date.uValue ?: existing.date,
                topic = update.topic.uValue ?: existing.topic,
                startTime = update.startTime.uValue ?: existing.startTime,
                endTime = update.endTime.uValue ?: existing.endTime,
                roomId = update.roomId.uValue ?: existing.roomId,
                locationAddress = update.locationAddress.uValue ?: existing.locationAddress
            )
            MockLessonDataSource.updateLesson(updated)
            Result.success(updated)
        } catch (e: Exception) {
            logger?.e(tag, "updateLesson failed", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteLesson(lessonId: Int): Result<Unit> {
        logger?.i(tag, "deleteLesson started", "lessonId=$lessonId")
        MockDelayService.delay()
        return try {
            val deleted = MockLessonDataSource.deleteLesson(lessonId)
            if (deleted) Result.success(Unit)
            else Result.failure(NoSuchElementException("Lesson not found"))
        } catch (e: Exception) {
            logger?.e(tag, "deleteLesson failed", e)
            Result.failure(e)
        }
    }
}