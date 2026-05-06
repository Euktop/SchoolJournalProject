// SchoolAdminRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockSchoolDataSource
import stud.euktop.data.mock.filterStringParam
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchoolAdminRepositoryImpl @Inject constructor() : SchoolAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getSchools(schoolFilter: SchoolFilter): Result<List<School>> {
        logger?.i(tag, "getSchools started", "filter=$schoolFilter")
        MockDelayService.delay()
        return try {
            val filtered = MockSchoolDataSource.getAll()
                .filter {
                    filterStringParam(
                        schoolFilter.name to it.name,
                        schoolFilter.region to it.region,
                        schoolFilter.address to it.address
                    )
                }
            logger?.i(tag, "getSchools succeeded", "count=${filtered.size}")
            Result.success(filtered)
        } catch (e: Exception) {
            logger?.e(tag, "getSchools failed", e, "filter=$schoolFilter")
            Result.failure(e)
        }
    }
}