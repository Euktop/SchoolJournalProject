// TeacherRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockLessonDataSource
import stud.euktop.data.mock.MockTeacherDataSource
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.lesson.TeacherClassItem
import stud.euktop.domain.repository.TeacherRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherRepositoryImpl @Inject constructor() : TeacherRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getTeacherClasses(): Result<List<TeacherClassItem>> {
        logger?.i(tag, "getTeacherClasses started")
        MockDelayService.delay()
        return try {
            val classes = MockTeacherDataSource.getTeacherClasses()
            logger?.i(tag, "getTeacherClasses succeeded", "count=${classes.size}")
            Result.success(classes)
        } catch (e: Exception) {
            logger?.e(tag, "getTeacherClasses failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getTeacherLessons(): Result<List<Lesson>> {
        logger?.i(tag, "getTeacherLessons started")
        MockDelayService.delay()
        return try {
            val lessons = MockLessonDataSource.getAll()
            logger?.i(tag, "getTeacherLessons succeeded", "count=${lessons.size}")
            Result.success(lessons)
        } catch (e: Exception) {
            logger?.e(tag, "getTeacherLessons failed", e)
            Result.failure(e)
        }
    }
}