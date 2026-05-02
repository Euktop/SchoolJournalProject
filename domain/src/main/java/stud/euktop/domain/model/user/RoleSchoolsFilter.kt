package stud.euktop.domain.model.user

import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.school.SchoolFilter

/**
 * Связь роли пользователя со школой.
 * Для глобального администратора school = null.
 */
data class RoleSchoolsFilter(
    val role: Role? = null,
    val school: Int? = null
) {
/*    companion object {
        fun exec(roleSchools: RoleSchools?) = if (roleSchools==null)null else
            RoleSchoolsFilter(
                role = roleSchools.role,
                school = SchoolFilter.exec(roleSchools.school)
            )
    }*/
}