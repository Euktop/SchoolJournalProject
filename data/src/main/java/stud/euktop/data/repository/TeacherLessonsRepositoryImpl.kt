// TeacherLessonsRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockTeacherDataSource
import stud.euktop.domain.model.lesson.TeacherLessonItem
import stud.euktop.domain.repository.TeacherLessonsRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherLessonsRepositoryImpl @Inject constructor() : TeacherLessonsRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getLessons(classId: Int, subjectId: Int): Result<List<TeacherLessonItem>> {
        logger?.i(tag, "getLessons started", "classId=$classId, subjectId=$subjectId")
        MockDelayService.delay()
        return try {
            val lessons = MockTeacherDataSource.getLessons(classId, subjectId)
            logger?.i(tag, "getLessons succeeded", "classId=$classId, subjectId=$subjectId, count=${lessons.size}")
            Result.success(lessons)
        } catch (e: Exception) {
            logger?.e(tag, "getLessons failed", e, "classId=$classId, subjectId=$subjectId")
            Result.failure(e)
        }
    }
}