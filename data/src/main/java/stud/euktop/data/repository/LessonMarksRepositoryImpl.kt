package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockLessonMarksDataSource
import stud.euktop.domain.model.attendance.StudentMarkItem
import stud.euktop.domain.repository.LessonMarksRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonMarksRepositoryImpl @Inject constructor() : LessonMarksRepository {
    override suspend fun getMarks(lessonId: Int): Result<List<StudentMarkItem>> {
        MockDelayService.delay()
        return Result.success(MockLessonMarksDataSource.getMarks(lessonId))
    }
}