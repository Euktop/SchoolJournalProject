package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockSchoolDataSource
import stud.euktop.domain.model.school.School
import stud.euktop.domain.repository.SchoolAdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchoolAdminRepositoryImpl @Inject constructor() : SchoolAdminRepository {
    override suspend fun getSchools(query: String): Result<List<School>> {
        MockDelayService.delay()
        val filtered = MockSchoolDataSource.getAll()
            .filter { query.isBlank() || it.name.contains(query, ignoreCase = true) }
        return Result.success(filtered)
    }
}