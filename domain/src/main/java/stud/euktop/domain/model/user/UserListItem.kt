package stud.euktop.domain.model.user

data class UserListItem(
    val userId: Int,
    val lastName: String,
    val firstName: String,
    val surName: String?,
    val email: String,
    val accountStatus: AccountStatus
)