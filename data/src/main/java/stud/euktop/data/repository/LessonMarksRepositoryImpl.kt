package stud.euktop.data.repository

import com.schooljournal.api.LessonsApi
import stud.euktop.data.map.toDomain
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.attendance.StudentMarkItem
import stud.euktop.domain.repository.LessonMarksRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonMarksRepositoryImpl @Inject constructor(
    private val lessonsApi: LessonsApi,
    private val errorHandler: ApiErrorHandler
) : LessonMarksRepository {

    override suspend fun getMarks(lessonId: Int): Result<List<StudentMarkItem>> =
        errorHandler.safeApiCall {
            val dtos = lessonsApi.apiLessonsLessonIdMarksGet(lessonId)
            dtos.map { it.toDomain() }
        }
}