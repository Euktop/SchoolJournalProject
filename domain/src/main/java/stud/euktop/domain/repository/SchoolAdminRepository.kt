package stud.euktop.domain.repository

import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter

interface SchoolAdminRepository {
    suspend fun getSchools(schoolFilter: SchoolFilter = SchoolFilter()): Result<List<School>>
}