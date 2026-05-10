package stud.euktop.domain.model.user

data class UserRef(
    val userId: Int,
    val lastName: String,
    val firstName: String,
    val surName: String?
) {
    companion object {
        fun createObject(
            userId: Int?,
            lastName: String?,
            firstName: String?,
            surName: String?
        ) = UserRef(
            userId = userId ?: 0,
            lastName = lastName ?: "",
            firstName = firstName ?: "",
            surName = surName
        )
    }
}