package stud.euktop.domain.model.user

import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.school.School
/**
 * Связь роли пользователя со школой.
 * Для глобального администратора school = null.
 */
data class RoleSchools(
    val role: Role,
    val school: School?
)