package stud.euktop.data.repository

import stud.euktop.data.MockData
import stud.euktop.domain.model.TeacherLessonItem
import stud.euktop.domain.repository.TeacherLessonsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherLessonsRepositoryImpl @Inject constructor() : TeacherLessonsRepository {
    override suspend fun getLessons(classId: Int, subjectId: Int): Result<List<TeacherLessonItem>> {
        return Result.success(MockData.lessons)
    }
}