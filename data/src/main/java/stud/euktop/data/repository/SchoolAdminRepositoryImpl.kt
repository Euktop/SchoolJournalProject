package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockSchoolDataSource
import stud.euktop.data.mock.filterStringParam
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.repository.SchoolAdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchoolAdminRepositoryImpl @Inject constructor() : SchoolAdminRepository {
    override suspend fun getSchools(schoolFilter: SchoolFilter): Result<List<School>> {
        MockDelayService.delay()
        val filtered = MockSchoolDataSource.getAll()
            .filter {
                filterStringParam(
                    schoolFilter.name to it.name,
                    schoolFilter.region to it.region,
                    schoolFilter.address to it.address
                )
            }
        return Result.success(filtered)
    }
}