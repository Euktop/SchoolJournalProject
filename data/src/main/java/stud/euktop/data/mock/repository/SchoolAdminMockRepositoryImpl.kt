package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockSchoolDataSource
import stud.euktop.data.mock.util.filterParam
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.school.SchoolUpdate
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchoolAdminMockRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler
) : SchoolAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getSchools(filter: SchoolFilter): Result<List<School>> {
        logger?.i(tag, "getSchools started", "filter=$filter")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val all = MockSchoolDataSource.getAll()
            val filtered = all.filter { school ->
                filterParam(
                    filter.name to school.name,
                    filter.region to school.region,
                    filter.address to school.address
                )
            }
            filtered.drop(filter.pagination.offset ?: 0)
                .take(filter.pagination.limit ?: Int.MAX_VALUE)
        }
    }

    override suspend fun getSchool(schoolId: Int): Result<School> {
        logger?.i(tag, "getSchool started", "schoolId=$schoolId")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            MockSchoolDataSource.getAll().find { it.schoolId == schoolId } ?: School(
                schoolId = schoolId,
                name = "Неизвестная школа",
                region = "Неизвестный регион",
                address = "Неизвестный адрес"
            )
        }
    }

    override suspend fun addSchool(school: School): Result<School> {
        logger?.i(tag, "addSchool started", "school=$school")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val newId = (MockSchoolDataSource.getAll().maxOfOrNull { it.schoolId } ?: 0) + 1
            val newSchool = school.copy(schoolId = newId)
            MockSchoolDataSource.add(newSchool)
            newSchool
        }
    }

    override suspend fun updateSchool(update: SchoolUpdate): Result<School> {
        logger?.i(tag, "updateSchool started", "update=$update")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val existing = MockSchoolDataSource.getAll().find { it.schoolId == update.schoolId }
                ?: School(update.schoolId, "Неизвестная школа", "Неизвестный регион", "Неизвестный адрес")
            val updated = existing.copy(
                name = update.name.uValue ?: existing.name,
                region = update.region.uValue ?: existing.region,
                address = update.address.uValue ?: existing.address
            )
            MockSchoolDataSource.update(updated)
            updated
        }
    }

    override suspend fun deleteSchool(schoolId: Int): Result<Unit> {
        logger?.i(tag, "deleteSchool started", "schoolId=$schoolId")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val deleted = MockSchoolDataSource.delete(schoolId)
            if (!deleted) {
                logger?.d(tag, "deleteSchool_warning", "School not found, returning success for idempotency")
            }
            Unit
        }
    }
}