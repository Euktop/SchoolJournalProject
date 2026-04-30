// data/src/main/java/stud/euktop/data/repository/HomeworkRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockHomeworkDataSource
import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.repository.HomeworkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeworkRepositoryImpl @Inject constructor() : HomeworkRepository {

    override suspend fun getHomeworkByTeacher(teacherId: Int): Result<List<Homework>> {
        MockDelayService.delay()
        return Result.success(MockHomeworkDataSource.getByTeacher(teacherId))
    }

    override suspend fun getHomeworkByStudent(studentId: Int): Result<List<Homework>> {
        MockDelayService.delay()
        return Result.success(MockHomeworkDataSource.getByStudent(studentId))
    }

    override suspend fun getHomeworkByLesson(lessonId: Int): Result<List<Homework>> {
        MockDelayService.delay()
        return Result.success(MockHomeworkDataSource.getByLesson(lessonId))
    }

    override suspend fun addHomework(homework: Homework): Result<Homework> {
        MockDelayService.delay()
        return Result.success(MockHomeworkDataSource.add(homework))
    }

    override suspend fun updateHomework(homework: Homework): Result<Homework> {
        MockDelayService.delay()
        MockHomeworkDataSource.update(homework)
        return Result.success(homework)
    }

    override suspend fun deleteHomework(homeworkId: Int): Result<Unit> {
        MockDelayService.delay()
        return if (MockHomeworkDataSource.delete(homeworkId)) Result.success(Unit)
        else Result.failure(NoSuchElementException("Homework not found"))
    }
}