package stud.euktop.domain.model.auth

import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Gender
import java.util.Date
/**
 * Профиль текущего авторизованного пользователя.
 * Возвращается сервером после успешного входа.
 */
data class Profile(
    val userId: Int,
    val lastName: String,
    val firstName: String,
    val surName: String?,
    val gender: Gender,
    val birthDay: Date?,
    val email: String,
    val phone: String?,
    val dateRegistration: Date,
    val accountStatus: AccountStatus,
    val roles: List<Role>
)