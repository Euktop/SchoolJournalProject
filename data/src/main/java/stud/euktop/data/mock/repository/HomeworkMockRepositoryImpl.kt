package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockClassDataSource
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockHomeworkDataSource
import stud.euktop.data.mock.data.MockLessonDataSource
import stud.euktop.data.mock.data.MockRoomDataSource
import stud.euktop.data.mock.data.MockSubjectDataSource
import stud.euktop.data.mock.data.MockUserDataSource
import stud.euktop.data.mock.util.filterParam
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.homework.HomeworkFilter
import stud.euktop.domain.model.homework.HomeworkFull
import stud.euktop.domain.model.homework.HomeworkMedia
import stud.euktop.domain.model.homework.HomeworkUpdate
import stud.euktop.domain.model.lesson.LessonFull
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserRef
import stud.euktop.domain.repository.HomeworkRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import java.io.File
import java.net.URLConnection
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeworkMockRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler
) : HomeworkRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getHomeworks(filter: HomeworkFilter): Result<List<Homework>> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "getHomeworks", "filter=$filter")
            MockDelayService.delay()
            val all = MockHomeworkDataSource.getAll()
            val descFilter = filter.description
            val filtered = all.filter { homework ->
                val lesson = MockLessonDataSource.getLesson(homework.lessonId)
                filterParam(
                    filter.homeworkId to homework.homeworkId,
                    filter.lessonId to homework.lessonId,
                    filter.teacherId to homework.createdByUserId,
                    filter.classId to (lesson?.classId),
                    filter.subjectId to (lesson?.subjectId)
                ) && (descFilter == null || homework.description.contains(
                    descFilter,
                    ignoreCase = true
                )) &&
                        (filter.createdFrom == null || homework.createdAt >= filter.createdFrom) &&
                        (filter.createdTo == null || homework.createdAt <= filter.createdTo)
            }
            filtered.drop(filter.pagination.offset ?: 0)
                .take(filter.pagination.limit ?: Int.MAX_VALUE)
                .ifEmpty { emptyList() }
        }

    override suspend fun getHomeworkById(id: Int): Result<Homework> = apiErrorHandler.safeApiCall {
        logger?.d(tag, "getHomeworkById", "id=$id")
        MockDelayService.delay()
        MockHomeworkDataSource.getById(id).takeIf { it.lessonId != 0 } ?: Homework(
            homeworkId = id,
            lessonId = 0,
            description = "Домашнее задание не найдено",
            createdAt = Date(),
            medias = emptyList(),
            createdByUserId = 0
        )
    }

    override suspend fun getHomeworkFullById(id: Int): Result<HomeworkFull> =
        apiErrorHandler.safeApiCall {
            logger?.d(tag, "getHomeworkFullById", "id=$id")
            MockDelayService.delay()
            val homework = getHomeworkById(id).getOrThrow()
            val lesson = MockLessonDataSource.getLesson(homework.lessonId)
            val classInfo = if (lesson != null) MockClassDataSource.get(lesson.classId) else null
            val subject = if (lesson != null) MockSubjectDataSource.get(lesson.subjectId) else null
            val teacherProfile =
                if (lesson != null) MockUserDataSource.getUser(lesson.teacherId) else null
            val room = lesson?.roomId?.let { MockRoomDataSource.get(it) }
            val createdByProfile = MockUserDataSource.getUser(homework.createdByUserId)

            val lessonFull = LessonFull(
                lessonId = lesson?.lessonId ?: 0,
                classInfo = classInfo ?: ClassInfo.createObject(0, 0, 0, "А", 2024, 2025, null),
                subject = subject ?: Subject.createObject(0, "Неизвестный предмет", null),
                teacher = UserRef(
                    teacherProfile?.userId ?: 0,
                    teacherProfile?.lastName ?: "Неизвестный",
                    teacherProfile?.firstName ?: "Учитель",
                    teacherProfile?.surName
                ),
                date = lesson?.date ?: Date(),
                topic = lesson?.topic ?: "Тема не указана",
                startTime = lesson?.startTime ?: "00:00",
                endTime = lesson?.endTime ?: "00:00",
                room = room,
                locationAddress = lesson?.locationAddress
            )
            val media = getMediaList(id).getOrElse { emptyList() }
            HomeworkFull(
                homeworkId = homework.homeworkId,
                lesson = lessonFull,
                description = homework.description,
                createdAt = homework.createdAt,
                createdBy = UserRef(
                    createdByProfile?.userId ?: 0,
                    createdByProfile?.lastName ?: "Неизвестный",
                    createdByProfile?.firstName ?: "Создатель",
                    createdByProfile?.surName
                ),
                media = media
            )
        }

    override suspend fun addHomework(homework: Homework): Result<Homework> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "addHomework", "homework=$homework")
            MockDelayService.delay()
            MockHomeworkDataSource.add(homework)
        }

    override suspend fun updateHomework(update: HomeworkUpdate): Result<Homework> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "updateHomework", "update=$update")
            MockDelayService.delay()
            val existing = MockHomeworkDataSource.getById(update.homeworkId)
            val updated =
                existing.copy(description = update.description.uValue ?: existing.description)
            MockHomeworkDataSource.update(updated)
            updated
        }

    override suspend fun deleteHomework(homeworkId: Int): Result<Unit> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "deleteHomework", "homeworkId=$homeworkId")
            MockDelayService.delay()
            val deleted = MockHomeworkDataSource.delete(homeworkId)
            if (!deleted) {
                logger?.d(
                    tag,
                    "deleteHomework_warning",
                    "Homework not found, returning success for idempotency"
                )
            }
            Unit
        }

    override suspend fun getMediaList(homeworkId: Int): Result<List<HomeworkMedia>> =
        apiErrorHandler.safeApiCall {
            logger?.d(tag, "getMediaList", "homeworkId=$homeworkId")
            MockDelayService.delay()
            MockHomeworkDataSource.getMedia(homeworkId).ifEmpty { emptyList() }
        }

    override suspend fun addMedia(homeworkId: Int, file: File): Result<HomeworkMedia> =
        apiErrorHandler.safeApiCall {
            logger?.i(
                tag,
                "addMedia",
                "homeworkId=$homeworkId, fileName=${file.name}, size=${file.length()}"
            )
            MockDelayService.delay()
            val contentType =
                URLConnection.guessContentTypeFromName(file.name) ?: "application/octet-stream"
            MockHomeworkDataSource.addMedia(
                homeworkId = homeworkId,
                fileName = file.name,
                contentType = contentType,
                fileSize = file.length().toInt()
            )
        }

    override suspend fun deleteMedia(mediaId: Int): Result<Unit> = apiErrorHandler.safeApiCall {
        logger?.i(tag, "deleteMedia", "mediaId=$mediaId")
        MockDelayService.delay()
        val deleted = MockHomeworkDataSource.deleteMedia(mediaId)
        if (!deleted) {
            logger?.d(
                tag,
                "deleteMedia_warning",
                "Media not found, returning success for idempotency"
            )
        }
        Unit
    }

    override suspend fun downloadMedia(mediaId: Int): Result<File> = apiErrorHandler.safeApiCall {
        logger?.i(tag, "downloadMedia", "mediaId=$mediaId")
        MockDelayService.delay()
        val file = MockHomeworkDataSource.getMediaFile(mediaId)
        logger?.d(
            tag,
            "downloadMedia_success",
            "File generated at ${file.absolutePath}, size=${file.length()}"
        )
        file
    }
}