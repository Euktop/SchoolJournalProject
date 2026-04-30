package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockTeacherDataSource
import stud.euktop.domain.model.lesson.TeacherLessonItem
import stud.euktop.domain.repository.TeacherLessonsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherLessonsRepositoryImpl @Inject constructor() : TeacherLessonsRepository {
    override suspend fun getLessons(classId: Int, subjectId: Int): Result<List<TeacherLessonItem>> {
        MockDelayService.delay()
        return Result.success(MockTeacherDataSource.getLessons(classId, subjectId))
    }
}