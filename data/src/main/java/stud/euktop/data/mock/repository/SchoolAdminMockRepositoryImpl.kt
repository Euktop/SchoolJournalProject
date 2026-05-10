// data/src/main/java/stud/euktop/data/mock/repository/SchoolAdminMockRepositoryImpl.kt
package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockSchoolDataSource
import stud.euktop.data.mock.util.filterParam
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.school.SchoolUpdate
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchoolAdminMockRepositoryImpl @Inject constructor() : SchoolAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getSchools(filter: SchoolFilter): Result<List<School>> {
        logger?.i(tag, "getSchools started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val all = MockSchoolDataSource.getAll()
            val filtered = all.filter { school ->
                filterParam(
                    filter.name to school.name,
                    filter.region to school.region,
                    filter.address to school.address
                )
            }
            val paged = filtered.drop(filter.pagination.offset ?: 0)
                .take(filter.pagination.limit ?: Int.MAX_VALUE)
            Result.success(paged)
        } catch (e: Exception) {
            logger?.e(tag, "getSchools failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getSchool(schoolId: Int): Result<School> {
        logger?.i(tag, "getSchool started", "schoolId=$schoolId")
        MockDelayService.delay()
        return try {
            val school = MockSchoolDataSource.getAll().find { it.schoolId == schoolId }
            if (school != null) Result.success(school)
            else Result.failure(NoSuchElementException("School not found"))
        } catch (e: Exception) {
            logger?.e(tag, "getSchool exception", e)
            Result.failure(e)
        }
    }

    override suspend fun addSchool(school: School): Result<School> {
        logger?.i(tag, "addSchool started", "school=$school")
        MockDelayService.delay()
        return try {
            val newId = (MockSchoolDataSource.getAll().maxOfOrNull { it.schoolId } ?: 0) + 1
            val newSchool = school.copy(schoolId = newId)
            MockSchoolDataSource.add(newSchool)
            Result.success(newSchool)
        } catch (e: Exception) {
            logger?.e(tag, "addSchool failed", e)
            Result.failure(e)
        }
    }

    override suspend fun updateSchool(update: SchoolUpdate): Result<School> {
        logger?.i(tag, "updateSchool started", "update=$update")
        MockDelayService.delay()
        return try {
            val existing = MockSchoolDataSource.getAll().find { it.schoolId == update.schoolId }
                ?: return Result.failure(NoSuchElementException("School not found"))
            val updated = existing.copy(
                name = update.name.uValue ?: existing.name,
                region = update.region.uValue ?: existing.region,
                address = update.address.uValue ?: existing.address
            )
            MockSchoolDataSource.update(updated)
            Result.success(updated)
        } catch (e: Exception) {
            logger?.e(tag, "updateSchool failed", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteSchool(schoolId: Int): Result<Unit> {
        logger?.i(tag, "deleteSchool started", "schoolId=$schoolId")
        MockDelayService.delay()
        return try {
            MockSchoolDataSource.delete(schoolId)
            Result.success(Unit)
        } catch (e: Exception) {
            logger?.e(tag, "deleteSchool failed", e)
            Result.failure(e)
        }
    }
}