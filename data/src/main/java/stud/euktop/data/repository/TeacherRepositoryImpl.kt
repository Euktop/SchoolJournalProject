package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockLessonDataSource
import stud.euktop.data.mock.MockTeacherDataSource
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.lesson.TeacherClassItem
import stud.euktop.domain.repository.TeacherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherRepositoryImpl @Inject constructor() : TeacherRepository {
    override suspend fun getTeacherClasses(): Result<List<TeacherClassItem>> {
        MockDelayService.delay()
        return Result.success(MockTeacherDataSource.getTeacherClasses())
    }

    override suspend fun getTeacherLessons(): Result<List<Lesson>> {
        MockDelayService.delay()
        return Result.success(MockLessonDataSource.getAll())
    }
}