package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockSubjectDataSource
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.model.school.SubjectUpdate
import stud.euktop.domain.repository.SubjectAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubjectAdminMockRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler
) : SubjectAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getSubjects(filter: SubjectFilter): Result<List<Subject>> {
        logger?.i(tag, "getSubjects started", "filter=$filter")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val all = MockSubjectDataSource.getAll()
            val filtered = all.filter { subject ->
                filter.name == null || subject.name.contains(filter.name ?: "", ignoreCase = true)
            }
            filtered.drop(filter.pagination.offset ?: 0)
                .take(filter.pagination.limit ?: Int.MAX_VALUE)
        }
    }

    override suspend fun getSubject(subjectId: Int): Result<Subject> {
        logger?.i(tag, "getSubject started", "subjectId=$subjectId")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            MockSubjectDataSource.get(subjectId) ?: throw NoSuchElementException("Subject not found")
        }
    }

    override suspend fun addSubject(subject: Subject): Result<Subject> {
        logger?.i(tag, "addSubject started", "subject=$subject")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            MockSubjectDataSource.add(subject)
        }
    }

    override suspend fun updateSubject(subject: SubjectUpdate): Result<Subject> {
        logger?.i(tag, "updateSubject started", "subjectUpdate=$subject")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val existing = MockSubjectDataSource.get(subject.subjectId)
                ?: throw NoSuchElementException("Subject not found")
            val updatedName = subject.name.uValue ?: existing.name
            val updatedDescription = subject.description.uValue ?: existing.description
            val updatedSubject = existing.copy(name = updatedName, description = updatedDescription)
            MockSubjectDataSource.update(updatedSubject)
            updatedSubject
        }
    }

    override suspend fun deleteSubject(subjectId: Int): Result<Unit> {
        logger?.i(tag, "deleteSubject started", "subjectId=$subjectId")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            if (!MockSubjectDataSource.delete(subjectId)) throw NoSuchElementException("Subject not found")
        }
    }
}