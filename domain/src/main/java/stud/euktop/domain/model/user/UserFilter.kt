package stud.euktop.domain.model.user

import stud.euktop.domain.model.common.Pagination

data class UserFilter(
    val fullName: String? = null,
    val role: Role? = null,
    val schoolId: Int? = null,
    val accountStatus: AccountStatus? = null,
    val pagination: Pagination = Pagination()
)
