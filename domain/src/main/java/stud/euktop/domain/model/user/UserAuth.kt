package stud.euktop.domain.model.user

// domain/model/user/UserAuth.kt
data class UserAuth(
    val userId: Int,
    val token: String,
    val roleIds: List<Int>
)