package stud.euktop.domain.model.user

import java.util.Date

data class UserProfile(
    val userId: Int,
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
) {
    companion object {
        fun createObject(
            userId: Int = 0,
            lastName: String = "",
            firstName: String = "",
            surName: String? = null,
            birthday: Date? = null,
            gender: Gender = Gender.NONE,
            email: String = "",
            phone: String? = null,
            dateRegistration: Date = Date(),
            accountStatus: AccountStatus = AccountStatus.PENDING,
            roles: List<UserRole> = emptyList()
        ) = UserProfile(
            userId = userId,
            lastName = lastName,
            firstName = firstName,
            surName = surName,
            birthday = birthday,
            gender = gender,
            email = email,
            phone = phone,
            dateRegistration = dateRegistration,
            accountStatus = accountStatus,
            roles = roles
        )
    }
}