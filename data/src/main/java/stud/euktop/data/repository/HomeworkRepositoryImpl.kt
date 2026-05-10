// data/src/main/java/stud/euktop/data/repository/HomeworkRepositoryImpl.kt
package stud.euktop.data.repository

import com.schooljournal.api.HomeworkApi
import com.schooljournal.model.UpdateHomeworkRequest
import stud.euktop.data.map.toCreateRequest
import stud.euktop.data.map.toDomain
import stud.euktop.data.map.toHomework
import stud.euktop.data.map.toHomeworkFull
import stud.euktop.data.map.toLocalDateTime
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.homework.HomeworkFilter
import stud.euktop.domain.model.homework.HomeworkFull
import stud.euktop.domain.model.homework.HomeworkMedia
import stud.euktop.domain.model.homework.HomeworkUpdate
import stud.euktop.domain.repository.HomeworkRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeworkRepositoryImpl @Inject constructor(
    private val homeworkApi: HomeworkApi,
    private val errorHandler: ApiErrorHandler
) : HomeworkRepository {

    override suspend fun getHomeworks(filter: HomeworkFilter): Result<List<Homework>> =
        errorHandler.safeApiCall {
            val dtos = homeworkApi.apiHomeworkFilterGet(
                lessonId = filter.lessonId,
                teacherId = filter.teacherId,
                studentId = filter.studentId,
                classId = filter.classId,
                subjectId = filter.subjectId,
                description = filter.description,
                dateFrom = filter.createdFrom?.toLocalDateTime(),
                dateTo = filter.createdTo?.toLocalDateTime(),
                offset = filter.pagination.offset,
                limit = filter.pagination.limit
            )
            dtos.map { it.toHomework() }
        }

    override suspend fun getHomeworkById(id: Int): Result<Homework> =
        errorHandler.safeApiCall {
            homeworkApi.apiHomeworkIdGet(id).toHomework()
        }

    override suspend fun getHomeworkFullById(id: Int): Result<HomeworkFull> =
        errorHandler.safeApiCall {
            val dto = homeworkApi.apiHomeworkIdGet(id)
            val media = getMediaList(id).getOrElse { emptyList() }
            dto.toHomeworkFull().copy(media = media)
        }

    override suspend fun addHomework(homework: Homework): Result<Homework> =
        errorHandler.safeApiCall {
            val request = homework.toCreateRequest()
            val result = homeworkApi.apiHomeworkPost(request)
            getHomeworkById(result.homeworkId ?: 0).getOrThrow()
        }

    override suspend fun updateHomework(update: HomeworkUpdate): Result<Homework> =
        errorHandler.safeApiCall {
            homeworkApi.apiHomeworkIdPut(
                id = update.homeworkId,
                updateHomeworkRequest = UpdateHomeworkRequest(description = update.description.uValue)
            )
            getHomeworkById(update.homeworkId).getOrThrow()
        }

    override suspend fun deleteHomework(homeworkId: Int): Result<Unit> =
        errorHandler.safeApiCall {
            homeworkApi.apiHomeworkIdDelete(homeworkId)
        }

    override suspend fun getMediaList(homeworkId: Int): Result<List<HomeworkMedia>> =
        errorHandler.safeApiCall {
            val dtos = homeworkApi.apiHomeworkHomeworkIdMediaGet(homeworkId)
            dtos.map { it.toDomain() }
        }

    override suspend fun addMedia(homeworkId: Int, file: File): Result<HomeworkMedia> =
        errorHandler.safeApiCall {
            val result = homeworkApi.apiHomeworkHomeworkIdMediaPost(homeworkId, file)
            result.toDomain()
        }

    override suspend fun deleteMedia(mediaId: Int): Result<Unit> =
        errorHandler.safeApiCall {
            homeworkApi.apiHomeworkMediaMediaIdDelete(mediaId)
        }

    override suspend fun downloadMedia(mediaId: Int): Result<File> =
        errorHandler.safeApiCall {
            homeworkApi.apiHomeworkMediaMediaIdGet(mediaId)
        }
}