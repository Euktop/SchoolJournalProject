package stud.euktop.domain.repository

import stud.euktop.domain.model.school.School

interface SchoolAdminRepository {
    suspend fun getSchools(query: String = ""): Result<List<School>>
}