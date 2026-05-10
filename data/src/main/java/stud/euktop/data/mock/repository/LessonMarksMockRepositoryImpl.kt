// LessonMarksMockRepositoryImpl.kt
package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockLessonMarksDataSource
import stud.euktop.domain.model.attendance.StudentMarkItem
import stud.euktop.domain.repository.LessonMarksRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonMarksMockRepositoryImpl @Inject constructor() : LessonMarksRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getMarks(lessonId: Int): Result<List<StudentMarkItem>> {
        logger?.i(tag, "getMarks started", "lessonId=$lessonId")
        MockDelayService.delay()
        return try {
            val marks = MockLessonMarksDataSource.getMarks(lessonId)
            Result.success(marks)
        } catch (e: Exception) {
            logger?.e(tag, "getMarks failed", e)
            Result.failure(e)
        }
    }
}