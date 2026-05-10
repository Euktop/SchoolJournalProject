// domain/src/main/java/stud/euktop/domain/model/school/ClassInfoFilter.kt
package stud.euktop.domain.model.school

import stud.euktop.domain.model.common.Pagination

data class ClassInfoFilter(
    val schoolId: Int? = null,
    val query: String? = null,
    val teacherId: Int? = null,
    val academicYearStart: Int? = null,
    val academicYearEnd: Int? = null,
    val pagination: Pagination = Pagination()
)