package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockClassDataSource
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockLessonDataSource
import stud.euktop.data.mock.data.MockRoomDataSource
import stud.euktop.data.mock.data.MockSubjectDataSource
import stud.euktop.data.mock.data.MockUserDataSource
import stud.euktop.data.mock.util.filterParam
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.lesson.LessonFilter
import stud.euktop.domain.model.lesson.LessonFull
import stud.euktop.domain.model.lesson.LessonUpdate
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserRef
import stud.euktop.domain.repository.LessonRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonMockRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler
) : LessonRepository {
    private val tag = this.toSimpleTag()

    private suspend fun enrichToFull(lesson: Lesson): LessonFull {
        val classInfo = MockClassDataSource.get(lesson.classId) ?: ClassInfo.createObject(
            0,
            0,
            0,
            "А",
            2024,
            2025,
            null
        )
        val subject = MockSubjectDataSource.get(lesson.subjectId) ?: Subject.createObject(
            0,
            "Неизвестный предмет",
            null
        )
        val teacher = MockUserDataSource.getUser(lesson.teacherId)
        val room = lesson.roomId?.let { MockRoomDataSource.get(it) }
        return LessonFull(
            lessonId = lesson.lessonId,
            classInfo = classInfo,
            subject = subject,
            teacher = UserRef(
                teacher?.userId ?: 0,
                teacher?.lastName ?: "Неизвестный",
                teacher?.firstName ?: "Учитель",
                teacher?.surName
            ),
            date = lesson.date,
            topic = lesson.topic ?: "Тема не указана",
            startTime = lesson.startTime,
            endTime = lesson.endTime,
            room = room,
            locationAddress = lesson.locationAddress
        )
    }

    override suspend fun getLesson(lessonId: Int): Result<LessonFull> {
        logger?.i(tag, "getLesson started", "lessonId=$lessonId")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val lesson = MockLessonDataSource.getLesson(lessonId)
            if (lesson != null) {
                enrichToFull(lesson)
            } else {
                LessonFull.createObject(
                    lessonId = lessonId,
                    classInfo = ClassInfo.createObject(0, 0, 0, "А", 2024, 2025, null),
                    subject = Subject.createObject(0, "Неизвестный предмет", null),
                    teacher = UserRef.createObject(0, "Неизвестный", "Учитель", null),
                    date = null,
                    topic = "Тема не указана",
                    startTime = "00:00",
                    endTime = "00:00",
                    room = Room.createObject(0, 0, "Неизвестный кабинет"),
                    locationAddress = null
                )
            }
        }
    }

    override suspend fun getLessons(filter: LessonFilter): Result<List<LessonFull>> {
        logger?.i(tag, "getLessons started", "filter=$filter")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
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
            val enriched = paged.map { enrichToFull(it) }
            if (enriched.isEmpty()) {
                if (all.isNotEmpty()) listOf(enrichToFull(all.first())) else listOf(
                    LessonFull.createObject(
                        lessonId = 0,
                        classInfo = ClassInfo.createObject(0, 0, 0, "А", 2024, 2025, null),
                        subject = Subject.createObject(0, "Неизвестный предмет", null),
                        teacher = UserRef.createObject(0, "Неизвестный", "Учитель", null),
                        date = null,
                        topic = "Тема не указана",
                        startTime = "00:00",
                        endTime = "00:00",
                        room = Room.createObject(0, 0, "Неизвестный кабинет"),
                        locationAddress = null
                    )
                )
            } else enriched
        }
    }

    override suspend fun addLesson(lesson: Lesson): Result<LessonFull> {
        logger?.i(tag, "addLesson started", "lesson=$lesson")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val newLesson = MockLessonDataSource.addLesson(lesson)
            enrichToFull(newLesson)
        }
    }

    override suspend fun updateLesson(update: LessonUpdate): Result<Lesson> {
        logger?.i(tag, "updateLesson started", "update=$update")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val existing = MockLessonDataSource.getLesson(update.lessonId)
                ?: Lesson(update.lessonId, 0, 0, 0, Date(), null, "00:00", "00:00", null, null)
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
            updated
        }
    }

    override suspend fun deleteLesson(lessonId: Int): Result<Unit> {
        logger?.i(tag, "deleteLesson started", "lessonId=$lessonId")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val deleted = MockLessonDataSource.deleteLesson(lessonId)
            if (!deleted) {
                logger?.d(
                    tag,
                    "deleteLesson_warning",
                    "Lesson not found, returning success for idempotency"
                )
            }
            Unit
        }
    }
}