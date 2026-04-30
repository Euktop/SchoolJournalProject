package stud.euktop.domain.model.user

/**
 * Статус учётной записи пользователя.
 * @param ACTIVE: активна
 * @param DELETED: удалена
 * @param PENDING: ожидает подтверждения
 * @param BLOCKED: заблокирована
 */
enum class AccountStatus {
    ACTIVE,
    DELETED,
    PENDING,
    BLOCKED,
}
