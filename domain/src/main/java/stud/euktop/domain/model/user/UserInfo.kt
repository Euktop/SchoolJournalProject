package stud.euktop.domain.model.user

import stud.euktop.domain.model.common.BaseModel
import stud.euktop.domain.model.common.Defaults
import java.util.Date

/**
 * Полная информация о пользователе (без пароля).
 * Используется для отображения профиля, списков пользователей и т.д.
 */
data class UserInfo(
    val userId: Int = Defaults.ID_DEFAULT,
    val lastName: String = "",
    val firstName: String = "",
    val surName: String? = null,
    val birthday: Date? = null,
    val gender: Gender = Gender.NONE,
    val email: String = "",
    val phone: String? = null,
    val roles: List<RoleSchools> = emptyList(),
    val dateRegistration: Date = Date(),
    val accountStatus: AccountStatus = AccountStatus.DELETED
) : BaseModel {
    override val idKey: Int
        get() = userId
    val fullName
        get() = "$lastName $firstName $surName"
}
