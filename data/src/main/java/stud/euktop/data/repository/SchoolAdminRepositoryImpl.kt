package stud.euktop.data.repository

import com.schooljournal.api.SchoolsApi
import stud.euktop.data.map.toCreateRequest
import stud.euktop.data.map.toDomain
import stud.euktop.data.map.toUpdateRequest
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.school.SchoolUpdate
import stud.euktop.domain.repository.SchoolAdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchoolAdminRepositoryImpl @Inject constructor(
    private val schoolsApi: SchoolsApi,
    private val errorHandler: ApiErrorHandler
) : SchoolAdminRepository {

    override suspend fun getSchools(filter: SchoolFilter): Result<List<School>> =
        errorHandler.safeApiCall {
            schoolsApi.apiSchoolsGet(
                name = filter.name,
                region = filter.region,
                address = filter.address,
                offset = filter.pagination.offset,
                limit = filter.pagination.limit
            ).map { it.toDomain() }
        }

    override suspend fun getSchool(schoolId: Int): Result<School> =
        errorHandler.safeApiCall {
            schoolsApi.apiSchoolsIdGet(schoolId).toDomain()
        }

    override suspend fun addSchool(school: School): Result<School> =
        errorHandler.safeApiCall {
            val request = school.toCreateRequest()
            val result = schoolsApi.apiSchoolsPost(request)
            getSchool(result.schoolId ?: 0).getOrThrow()
        }

    override suspend fun updateSchool(update: SchoolUpdate): Result<School> =
        errorHandler.safeApiCall {
            val request = update.toUpdateRequest()
            schoolsApi.apiSchoolsIdPut(update.schoolId, request)
            getSchool(update.schoolId).getOrThrow()
        }

    override suspend fun deleteSchool(schoolId: Int): Result<Unit> =
        errorHandler.safeApiCall {
            schoolsApi.apiSchoolsIdDelete(schoolId)
        }
}