package stud.euktop.schooljournal.presentation.common.filter.user

import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserFilter

data class AppUserFilter(
    val fullName: String? = null,
    val role: Role? = null,
    val school: School? = null,
    val schoolFilter: SchoolFilter? = null,
    val accountStatus: AccountStatus? = null,
    val pagination: Pagination = Pagination()
) {
    fun toDomainFilter(): UserFilter = UserFilter(
        fullName = fullName,
        role = role,
        schoolId = school?.schoolId,
        accountStatus = accountStatus,
        pagination = pagination
    )
}

fun UserFilter.toApp(school: School?) = AppUserFilter(
    fullName = fullName,
    role = role,
    school = school,
    schoolFilter = SchoolFilter(),
    accountStatus = accountStatus,
    pagination = pagination,
)