package stud.euktop.domain.model.user

import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter

/**
 * Полная информация о пользователе (без пароля).
 * Используется для отображения профиля, списков пользователей и т.д.
 */
data class UserInfoFilter(
    val fullName: String? = null,
    val role: Role? = null,
    val school: School? = null,
    var schoolFilter: SchoolFilter = SchoolFilter(),
    val accountStatus: AccountStatus? = null,
)