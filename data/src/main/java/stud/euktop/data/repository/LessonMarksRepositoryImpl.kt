package stud.euktop.data.repository

import stud.euktop.data.MockData
import stud.euktop.domain.model.StudentMarkItem
import stud.euktop.domain.repository.LessonMarksRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonMarksRepositoryImpl @Inject constructor() : LessonMarksRepository {
    override suspend fun getMarks(lessonId: Int): Result<List<StudentMarkItem>> {
        MockData.delay()
        return Result.success(MockData.marks)
    }
}