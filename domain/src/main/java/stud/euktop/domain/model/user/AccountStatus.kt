package stud.euktop.domain.model.user

/**
 * Статус учётной записи пользователя.
 * @param ACTIVE: активна
 * @param DELETED: удалена
 * @param PENDING: ожидает подтверждения
 * @param BLOCKED: заблокирована
 */
enum class AccountStatus(val id: Int) {
    ACTIVE(1),
    DELETED(2),
    PENDING(3),
    BLOCKED(4);

    companion object {
        fun fromId(id: Int) = entries.find { it.id == id }
    }
}