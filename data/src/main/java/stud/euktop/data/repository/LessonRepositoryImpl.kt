package stud.euktop.data.repository

import com.schooljournal.api.LessonsApi
import stud.euktop.data.map.toCreateRequest
import stud.euktop.data.map.toDomain
import stud.euktop.data.map.toLessonFull
import stud.euktop.data.map.toLocalDateTime
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.lesson.LessonFilter
import stud.euktop.domain.model.lesson.LessonFull
import stud.euktop.domain.model.lesson.LessonUpdate
import stud.euktop.domain.repository.LessonRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRepositoryImpl @Inject constructor(
    private val lessonsApi: LessonsApi,
    private val errorHandler: ApiErrorHandler
) : LessonRepository {

    override suspend fun getLesson(lessonId: Int): Result<LessonFull> =
        errorHandler.safeApiCall {
            lessonsApi.apiLessonsIdGet(lessonId).toDomain()
        }

    override suspend fun getLessons(filter: LessonFilter): Result<List<LessonFull>> =
        errorHandler.safeApiCall {
            lessonsApi.apiLessonsFilterGet(
                classId = filter.classId,
                subjectId = filter.subjectId,
                teacherId = filter.teacherId,
                dateFrom = filter.dateFrom?.toLocalDateTime(),
                dateTo = filter.dateTo?.toLocalDateTime(),
                offset = filter.pagination.offset,
                limit = filter.pagination.limit
            ).map { it.toLessonFull() }
        }

    override suspend fun addLesson(lesson: Lesson): Result<LessonFull> =
        errorHandler.safeApiCall {
            val request = lesson.toCreateRequest()
            val result = lessonsApi.apiLessonsPost(request)
            getLesson(result.lessonId ?: 0).getOrThrow()
        }

    override suspend fun updateLesson(update: LessonUpdate): Result<Lesson> =
        errorHandler.safeApiCall {
            lessonsApi.apiLessonsIdPatch(
                id = update.lessonId,
                topic = update.topic.uValue,
                updateTopic = update.topic.isUpdate,
                startTime = update.startTime.uValue,
                updateStartTime = update.startTime.isUpdate,
                endTime = update.endTime.uValue,
                updateEndTime = update.endTime.isUpdate,
                roomId = update.roomId.uValue,
                updateRoomId = update.roomId.isUpdate,
                locationAddress = update.locationAddress.uValue,
                updateLocationAddress = update.locationAddress.isUpdate
            ).toDomain()
        }

    override suspend fun deleteLesson(lessonId: Int): Result<Unit> =
        errorHandler.safeApiCall {
            lessonsApi.apiLessonsIdDelete(lessonId)
        }
}