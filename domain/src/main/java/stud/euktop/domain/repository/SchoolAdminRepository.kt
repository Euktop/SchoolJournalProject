package stud.euktop.domain.repository

import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.school.SchoolUpdate

interface SchoolAdminRepository {
    suspend fun getSchools(filter: SchoolFilter = SchoolFilter()): Result<List<School>>
    suspend fun getSchool(schoolId: Int): Result<School>
    suspend fun addSchool(school: School): Result<School>
    suspend fun updateSchool(update: SchoolUpdate): Result<School>
    suspend fun deleteSchool(schoolId: Int): Result<Unit>
}