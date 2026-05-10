// data/src/main/java/stud/euktop/data/repository/SubjectAdminMockRepositoryImpl.kt
package stud.euktop.data.repository

import com.schooljournal.api.SubjectsApi
import com.schooljournal.model.CreateSubjectRequest
import stud.euktop.data.map.toDomain
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.model.school.SubjectUpdate
import stud.euktop.domain.repository.SubjectAdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubjectAdminRepositoryImpl @Inject constructor(
    private val subjectsApi: SubjectsApi,
    private val errorHandler: ApiErrorHandler
) : SubjectAdminRepository {

    override suspend fun getSubjects(filter: SubjectFilter): Result<List<Subject>> =
        errorHandler.safeApiCall {
            val dtos = subjectsApi.apiSubjectsFilterGet(
                name = filter.name,
                offset = filter.pagination.offset,
                limit = filter.pagination.limit
            )
            dtos.map { it.toDomain() }
        }

    override suspend fun getSubject(subjectId: Int): Result<Subject> =
        errorHandler.safeApiCall {
            val dto = subjectsApi.apiSubjectsIdGet(subjectId)
            dto.toDomain()
        }

    override suspend fun addSubject(subject: Subject): Result<Subject> =
        errorHandler.safeApiCall {
            val request = CreateSubjectRequest(
                name = subject.name,
                description = subject.description
            )
            subjectsApi.apiSubjectsPost(request).toDomain()
        }

    override suspend fun updateSubject(subject: SubjectUpdate): Result<Subject> =
        errorHandler.safeApiCall {
            subjectsApi.apiSubjectsIdPatch(
                id = subject.subjectId,
                name = subject.name.uValue,
                description = subject.description.uValue,
                updateDescription = subject.description.isUpdate
            ).toDomain()
        }

    override suspend fun deleteSubject(subjectId: Int): Result<Unit> =
        errorHandler.safeApiCall {
            subjectsApi.apiSubjectsIdDelete(subjectId)
        }
}