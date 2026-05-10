package stud.euktop.domain.model.user

import java.util.Date

data class UserProfile(
    val userId: Int = 0,
    val lastName: String,
    val firstName: String,
    val surName: String?,
    val birthday: Date?,
    val gender: Gender,
    val email: String,
    val phone: String?,
    val dateRegistration: Date,
    val accountStatus: AccountStatus,
    val roles: List<UserRole>
)