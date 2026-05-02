package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockSubjectDataSource
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.repository.SubjectAdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubjectAdminRepositoryImpl @Inject constructor() : SubjectAdminRepository {
    override suspend fun getSubjects(filter: SubjectFilter): Result<List<Subject>> {
        MockDelayService.delay()
        return Result.success(MockSubjectDataSource.getAll())
    }

    override suspend fun getSubject(subjectId: Int): Result<Subject> {
        MockDelayService.delay()
        return MockSubjectDataSource.get(subjectId)?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("Subject not found"))
    }

    override suspend fun addSubject(subject: Subject): Result<Subject> {
        MockDelayService.delay()
        return Result.success(MockSubjectDataSource.add(subject))
    }

    override suspend fun updateSubject(subject: Subject): Result<Subject> {
        MockDelayService.delay()
        MockSubjectDataSource.update(subject)
        return Result.success(subject)
    }

    override suspend fun deleteSubject(subjectId: Int): Result<Unit> {
        MockDelayService.delay()
        return if (MockSubjectDataSource.delete(subjectId)) Result.success(Unit)
        else Result.failure(NoSuchElementException("Subject not found"))
    }
}