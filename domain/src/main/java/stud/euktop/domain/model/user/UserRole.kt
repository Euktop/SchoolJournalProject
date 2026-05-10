package stud.euktop.domain.model.user

import java.util.Date

data class UserRole(
    val role: Role,
    val schoolId: Int?,
    val assignedAt: Date
)