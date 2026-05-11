package stud.euktop.domain.model.user

data class UserListItem(
    val userId: Int,
    val lastName: String,
    val firstName: String,
    val surName: String?,
    val email: String,
    val accountStatus: AccountStatus
) {
    companion object {
        fun createObject(
            userId: Int? = null,
            lastName: String? = null,
            firstName: String? = null,
            surName: String? = null,
            email: String? = null,
            accountStatus: AccountStatus? = null
        ) = UserListItem(
            userId = userId ?: 0,
            lastName = lastName ?: "",
            firstName = firstName ?: "",
            surName = surName ?: "",
            email = email ?: "",
            accountStatus = accountStatus ?: AccountStatus.PENDING
        )
    }
}