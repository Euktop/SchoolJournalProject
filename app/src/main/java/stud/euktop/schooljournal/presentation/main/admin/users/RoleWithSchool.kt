package stud.euktop.schooljournal.presentation.main.admin.users

import stud.euktop.domain.model.user.Role

data class RoleWithSchool(
    val role: Role,
    val schoolId: Int?,
    val schoolName: String? = null
)