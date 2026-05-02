// data/src/main/java/stud/euktop/data/repository/HomeworkRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockHomeworkDataSource
import stud.euktop.domain.model.common.DataError
import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.homework.HomeworkFilter
import stud.euktop.domain.repository.HomeworkRepository
import stud.euktop.data.mock.filterParam
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeworkRepositoryImpl @Inject constructor() : HomeworkRepository {
    override suspend fun getHomeworks(filter: HomeworkFilter): Result<List<Homework>> {
        MockDelayService.delay()
        return Result.success(MockHomeworkDataSource.getAll().filter {
            filterParam(
                filter.lesson to it.lesson.lessonId,
                filter.createdByUser to it.createdByUser.userId,
            )
                    && (filter.createdDiff.dateStart == null || filter.createdDiff.dateStart!! <= it.createdAt)
                    && (filter.createdDiff.dateEnd == null || filter.createdDiff.dateEnd!! >= it.createdAt)
        })
    }


    override suspend fun getHomeworkById(id: Int): Result<Homework> {
        MockDelayService.delay()
        val result = MockHomeworkDataSource.getAll().firstOrNull { it.homeworkId == id }
        return if (result != null) Result.success(result) else Result.failure(DataError.Unknown(null))
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