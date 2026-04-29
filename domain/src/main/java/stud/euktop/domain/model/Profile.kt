package stud.euktop.domain.model

import java.util.Date

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
    val accountStatusId: AccountStatus,
    val roles: List<Role>
)