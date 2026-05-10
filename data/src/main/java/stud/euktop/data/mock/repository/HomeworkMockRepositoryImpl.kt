package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockClassDataSource
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockHomeworkDataSource
import stud.euktop.data.mock.data.MockLessonDataSource
import stud.euktop.data.mock.data.MockRoomDataSource
import stud.euktop.data.mock.data.MockSubjectDataSource
import stud.euktop.data.mock.data.MockUserDataSource
import stud.euktop.data.mock.util.filterParam
import stud.euktop.domain.model.common.DataError
import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.homework.HomeworkFilter
import stud.euktop.domain.model.homework.HomeworkFull
import stud.euktop.domain.model.homework.HomeworkMedia
import stud.euktop.domain.model.homework.HomeworkUpdate
import stud.euktop.domain.model.lesson.LessonFull
import stud.euktop.domain.model.user.UserRef
import stud.euktop.domain.repository.HomeworkRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import java.io.File
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeworkMockRepositoryImpl @Inject constructor() : HomeworkRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getHomeworks(filter: HomeworkFilter): Result<List<Homework>> {
        logger?.i(tag, "getHomeworks started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val all = MockHomeworkDataSource.getAll()
            val descFilter = filter.description // локальная переменная для smart cast
            val filtered = all.filter { homework ->
                val lesson = MockLessonDataSource.getLesson(homework.lessonId)
                filterParam(
                    filter.homeworkId to homework.homeworkId,
                    filter.lessonId to homework.lessonId,
                    filter.teacherId to homework.createdByUserId,
                    filter.classId to (lesson?.classId),
                    filter.subjectId to (lesson?.subjectId)
                ) && (descFilter == null || homework.description.contains(descFilter, ignoreCase = true)) &&
                        (filter.createdFrom == null || homework.createdAt >= filter.createdFrom) &&
                        (filter.createdTo == null || homework.createdAt <= filter.createdTo)
            }
            val paged = filtered.drop(filter.pagination.offset ?: 0)
                .take(filter.pagination.limit ?: Int.MAX_VALUE)
            Result.success(paged)
        } catch (e: Exception) {
            logger?.e(tag, "getHomeworks failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getHomeworkById(id: Int): Result<Homework> {
        logger?.i(tag, "getHomeworkById started", "id=$id")
        MockDelayService.delay()
        return try {
            val homework = MockHomeworkDataSource.getById(id)
            if (homework != null) Result.success(homework)
            else Result.failure(DataError.RecordNotFound("Homework not found"))
        } catch (e: Exception) {
            logger?.e(tag, "getHomeworkById failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getHomeworkFullById(id: Int): Result<HomeworkFull> {
        logger?.i(tag, "getHomeworkFullById started", "id=$id")
        MockDelayService.delay()
        return try {
            val homework = getHomeworkById(id).getOrThrow()
            val lesson = MockLessonDataSource.getLesson(homework.lessonId)
                ?: throw DataError.RecordNotFound("Lesson not found")
            val classInfo = MockClassDataSource.get(lesson.classId)
                ?: throw DataError.RecordNotFound("Class not found")
            val subject = MockSubjectDataSource.get(lesson.subjectId)
                ?: throw DataError.RecordNotFound("Subject not found")
            val teacherProfile = MockUserDataSource.getUser(lesson.teacherId)
                ?: throw DataError.RecordNotFound("Teacher not found")
            val room = lesson.roomId?.let { MockRoomDataSource.get(it) }
            val createdByProfile = MockUserDataSource.getUser(homework.createdByUserId)
                ?: throw DataError.RecordNotFound("CreatedBy user not found")

            val lessonFull = LessonFull(
                lessonId = lesson.lessonId,
                classInfo = classInfo,
                subject = subject,
                teacher = UserRef(
                    teacherProfile.userId,
                    teacherProfile.lastName,
                    teacherProfile.firstName,
                    teacherProfile.surName
                ),
                date = lesson.date,
                topic = lesson.topic ?: "",
                startTime = lesson.startTime,
                endTime = lesson.endTime,
                room = room,
                locationAddress = lesson.locationAddress
            )

            val media = getMediaList(id).getOrElse { emptyList() }

            val full = HomeworkFull(
                homeworkId = homework.homeworkId,
                lesson = lessonFull,
                description = homework.description,
                createdAt = homework.createdAt,
                createdBy = UserRef(
                    createdByProfile.userId,
                    createdByProfile.lastName,
                    createdByProfile.firstName,
                    createdByProfile.surName
                ),
                media = media
            )
            Result.success(full)
        } catch (e: Exception) {
            logger?.e(tag, "getHomeworkFullById failed", e, "id=$id")
            Result.failure(e)
        }
    }

    override suspend fun addHomework(homework: Homework): Result<Homework> {
        logger?.i(tag, "addHomework started", "homework=$homework")
        MockDelayService.delay()
        return try {
            val newHomework = MockHomeworkDataSource.add(homework)
            Result.success(newHomework)
        } catch (e: Exception) {
            logger?.e(tag, "addHomework failed", e)
            Result.failure(e)
        }
    }

    override suspend fun updateHomework(update: HomeworkUpdate): Result<Homework> {
        logger?.i(tag, "updateHomework started", "update=$update")
        MockDelayService.delay()
        return try {
            val existing = MockHomeworkDataSource.getById(update.homeworkId)
                ?: return Result.failure(NoSuchElementException("Homework not found"))
            val updated = existing.copy(description = update.description.uValue ?: existing.description)
            MockHomeworkDataSource.update(updated)
            Result.success(updated)
        } catch (e: Exception) {
            logger?.e(tag, "updateHomework failed", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteHomework(homeworkId: Int): Result<Unit> {
        logger?.i(tag, "deleteHomework started", "homeworkId=$homeworkId")
        MockDelayService.delay()
        return try {
            if (MockHomeworkDataSource.delete(homeworkId)) Result.success(Unit)
            else Result.failure(NoSuchElementException("Homework not found"))
        } catch (e: Exception) {
            logger?.e(tag, "deleteHomework failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getMediaList(homeworkId: Int): Result<List<HomeworkMedia>> {
        logger?.i(tag, "getMediaList started", "homeworkId=$homeworkId")
        MockDelayService.delay()
        return try {
            Result.success(MockHomeworkDataSource.getMedia(homeworkId))
        } catch (e: Exception) {
            logger?.e(tag, "getMediaList failed", e)
            Result.failure(e)
        }
    }

    override suspend fun addMedia(homeworkId: Int, file: File): Result<HomeworkMedia> {
        logger?.i(tag, "addMedia started", "homeworkId=$homeworkId")
        MockDelayService.delay()
        return try {
            val media = HomeworkMedia(0, file.name, "application/octet-stream", file.length().toInt(), Date())
            val newMedia = MockHomeworkDataSource.addMedia(homeworkId, media)
            Result.success(newMedia)
        } catch (e: Exception) {
            logger?.e(tag, "addMedia failed", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteMedia(mediaId: Int): Result<Unit> {
        logger?.i(tag, "deleteMedia started", "mediaId=$mediaId")
        MockDelayService.delay()
        return try {
            MockHomeworkDataSource.deleteMedia(mediaId)
            Result.success(Unit)
        } catch (e: Exception) {
            logger?.e(tag, "deleteMedia failed", e)
            Result.failure(e)
        }
    }

    override suspend fun downloadMedia(mediaId: Int): Result<File> {
        logger?.i(tag, "downloadMedia started", "mediaId=$mediaId")
        MockDelayService.delay()
        return try {
            val media = MockHomeworkDataSource.getMediaById(mediaId)
                ?: return Result.failure(NoSuchElementException("Media not found"))
            val tempFile = File.createTempFile("mock_", media.fileName)
            tempFile.writeBytes(ByteArray(media.fileSize))
            Result.success(tempFile)
        } catch (e: Exception) {
            logger?.e(tag, "downloadMedia failed", e)
            Result.failure(e)
        }
    }
}