// data/src/main/java/stud/euktop/data/mock/repository/SubjectAdminMockRepositoryImpl.kt
package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockSubjectDataSource
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.model.school.SubjectUpdate
import stud.euktop.domain.repository.SubjectAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubjectAdminMockRepositoryImpl @Inject constructor() : SubjectAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getSubjects(filter: SubjectFilter): Result<List<Subject>> {
        logger?.i(tag, "getSubjects started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val all = MockSubjectDataSource.getAll()
            val filtered = all.filter { subject ->
                filter.name == null || subject.name.contains(filter.name ?: "", ignoreCase = true)
            }
            val paged = filtered.drop(filter.pagination.offset ?: 0)
                .take(filter.pagination.limit ?: Int.MAX_VALUE)
            logger?.i(tag, "getSubjects succeeded", "count=${paged.size}")
            Result.success(paged)
        } catch (e: Exception) {
            logger?.e(tag, "getSubjects failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getSubject(subjectId: Int): Result<Subject> {
        logger?.i(tag, "getSubject started", "subjectId=$subjectId")
        MockDelayService.delay()
        return try {
            val subject = MockSubjectDataSource.get(subjectId)
            if (subject != null) {
                logger?.i(tag, "getSubject succeeded", "subjectId=$subjectId")
                Result.success(subject)
            } else {
                val ex = NoSuchElementException("Subject not found")
                logger?.e(tag, "getSubject failed", ex, "subjectId=$subjectId not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "getSubject exception", e, "subjectId=$subjectId")
            Result.failure(e)
        }
    }

    override suspend fun addSubject(subject: Subject): Result<Subject> {
        logger?.i(tag, "addSubject started", "subject=$subject")
        MockDelayService.delay()
        return try {
            val newSubject = MockSubjectDataSource.add(subject)
            logger?.i(tag, "addSubject succeeded", "newId=${newSubject.subjectId}")
            Result.success(newSubject)
        } catch (e: Exception) {
            logger?.e(tag, "addSubject failed", e, "subject=$subject")
            Result.failure(e)
        }
    }

    override suspend fun updateSubject(subject: SubjectUpdate): Result<Subject> {
        logger?.i(tag, "updateSubject started", "subjectUpdate=$subject")
        MockDelayService.delay()
        return try {
            val existing = MockSubjectDataSource.get(subject.subjectId)
                ?: return Result.failure(NoSuchElementException("Subject not found"))

            val updatedName = subject.name.uValue ?: existing.name
            val updatedDescription = subject.description.uValue ?: existing.description

            val updatedSubject = existing.copy(
                name = updatedName,
                description = updatedDescription
            )
            MockSubjectDataSource.update(updatedSubject)
            logger?.i(tag, "updateSubject succeeded", "subjectId=${subject.subjectId}")
            Result.success(updatedSubject)
        } catch (e: Exception) {
            logger?.e(tag, "updateSubject failed", e, "subjectUpdate=$subject")
            Result.failure(e)
        }
    }

    override suspend fun deleteSubject(subjectId: Int): Result<Unit> {
        logger?.i(tag, "deleteSubject started", "subjectId=$subjectId")
        MockDelayService.delay()
        return try {
            val deleted = MockSubjectDataSource.delete(subjectId)
            if (deleted) {
                logger?.i(tag, "deleteSubject succeeded", "subjectId=$subjectId")
                Result.success(Unit)
            } else {
                val ex = NoSuchElementException("Subject not found")
                logger?.e(tag, "deleteSubject failed", ex, "subjectId=$subjectId not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "deleteSubject exception", e, "subjectId=$subjectId")
            Result.failure(e)
        }
    }
}