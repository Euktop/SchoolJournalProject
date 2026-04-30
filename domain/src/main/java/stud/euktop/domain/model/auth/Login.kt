package stud.euktop.domain.model.auth
/**
 * Данные для входа (логин и пароль в открытом виде – только на время отправки на сервер).
 */
data class Login(
    val email: String,
    val password: String
)