package stud.euktop.schooljournal.presentation.common.filter.classes

import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.model.school.ClassInfoFilter
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserListItem

data class AppClassInfoFilter(
    val school: School? = null,
    val schoolFilter: SchoolFilter? = null,
    val query: String? = null,
    val teacher: UserListItem? = null,
    val teacherFilter: UserFilter? = null,
    val academicYearStart: Int? = null,
    val academicYearEnd: Int? = null,
    val pagination: Pagination = Pagination()
) {
    fun toDomain() = ClassInfoFilter(
        schoolId = school?.schoolId,
        query = query,
        teacherId = teacher?.userId,
        academicYearStart = academicYearStart,
        academicYearEnd = academicYearEnd,
        pagination = pagination
    )
}