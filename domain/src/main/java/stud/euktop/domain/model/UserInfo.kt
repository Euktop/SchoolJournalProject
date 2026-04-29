package stud.euktop.domain.model

data class UserInfo(
    val userId: Int,
    val lastName: String,
    val firstName: String,
    val surName: String?,
    val email: String,
    val phone: String?,
    val roleNames: List<String>,  // "Администратор", "Учитель" и т.д.
    val accountStatus: AccountStatus
)
