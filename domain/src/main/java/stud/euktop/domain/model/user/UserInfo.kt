package stud.euktop.domain.model.user

/**
 * Полная информация о пользователе (без пароля).
 * Используется для отображения профиля, списков пользователей и т.д.
 */
data class UserInfo(
    val userId: Int,
    val lastName: String,
    val firstName: String,
    val surName: String?,
    val email: String,
    val phone: String?,
    val roles: List<RoleSchools>,
    val accountStatus: AccountStatus
) {
    val fullName
        get() = "$lastName $firstName $surName"
}
