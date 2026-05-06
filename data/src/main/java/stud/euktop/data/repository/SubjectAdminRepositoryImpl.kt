// SubjectAdminRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockSubjectDataSource
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.repository.SubjectAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubjectAdminRepositoryImpl @Inject constructor() : SubjectAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getSubjects(filter: SubjectFilter): Result<List<Subject>> {
        logger?.i(tag, "getSubjects started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val subjects = MockSubjectDataSource.getAll()
            logger?.i(tag, "getSubjects succeeded", "count=${subjects.size}")
            Result.success(subjects)
        } catch (e: Exception) {
            logger?.e(tag, "getSubjects failed", e, "filter=$filter")
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

    override suspend fun updateSubject(subject: Subject): Result<Subject> {
        logger?.i(tag, "updateSubject started", "subject=$subject")
        MockDelayService.delay()
        return try {
            MockSubjectDataSource.update(subject)
            logger?.i(tag, "updateSubject succeeded", "subjectId=${subject.subjectId}")
            Result.success(subject)
        } catch (e: Exception) {
            logger?.e(tag, "updateSubject failed", e, "subject=$subject")
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