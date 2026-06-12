// domain/src/main/java/stud/euktop/domain/model/common/DataError.kt
package stud.euktop.domain.model.common

/**
 * Специфичные ошибки предметной области, получаемые от сервера.
 * Соответствуют комбинации HTTP‑статусов и внутренних кодов ошибок (ErrorCode),
 * описанных в Swagger /api/errors.
 */
sealed class DataError : Throwable() {

    // ========== Сетевые / технические ==========
    data class HttpError(val code: Int, override val message: String?) : DataError()
    data class NetworkConnection(override val message: String?) : DataError()
    object EmptyBody : DataError()
    data class Unknown(override val message: String?) : DataError()

    // ========== Ошибки авторизации и прав (HTTP 401, 403) ==========
    /**
     * 401 Unauthorized – отсутствует или недействительный JWT‑токен.
     * (Не описан в таблице ErrorCode, но стандартный HTTP статус.)
     */
    data class Unauthorized(override val message: String?) : DataError()

    /**
     * HTTP 403 Forbidden – пользователь аутентифицирован, но не имеет права на действие.
     * Коды ErrorCode: 300, 301.
     */
    data class AccessDenied(override val message: String?) : DataError()

    // ========== Ошибки валидации и бизнес‑правил (HTTP 400) ==========
    /** ErrorCode 1 – Контекст сессии не установлен */
    data class SessionContextNotSet(override val message: String?) : DataError()

    /** ErrorCode 2 – Неверный логин или пароль */
    data class InvalidCredentials(override val message: String?) : DataError()

    /** ErrorCode 3, 102 – Пользователь не существует */
    data class UserNotFound(override val message: String?) : DataError()

    /** ErrorCode 4 – Пользователь удалён */
    data class UserDeleted(override val message: String?) : DataError()

    /** ErrorCode 5 – Пользователь не подтверждён */
    data class UserNotConfirmed(override val message: String?) : DataError()

    /** ErrorCode 6 – Пользователь заблокирован */
    data class UserBlocked(override val message: String?) : DataError()

    /** ErrorCode 7 – Пересечение интервалов классов */
    data class ClassTimeOverlap(override val message: String?) : DataError()

    /** ErrorCode 8 – Разрешение вышло за пределы диапазона */
    data class PermissionOutOfRange(override val message: String?) : DataError()

    /** ErrorCode 9 – Несколько преподавателей не могут быть главными в пересекающиеся периоды */
    data class MultiplePrimaryTeachers(override val message: String?) : DataError()

    /** ErrorCode 10 – Учитель не имеет права преподавать предмет */
    data class TeacherCannotTeachSubject(override val message: String?) : DataError()

    /** ErrorCode 11 – Студент не входит в класс */
    data class StudentNotInClass(override val message: String?) : DataError()

    /** ErrorCode 12 – Запись уже зафиксирована (прошло более 3 дней) */
    data class RecordAlreadyFixed(override val message: String?) : DataError()

    /** ErrorCode 13 – У пользователя нет прав на оценивание */
    data class NoPermissionToGrade(override val message: String?) : DataError()

    /** ErrorCode 14 – Поле обязательно для заполнения */
    data class FieldRequired(override val message: String?) : DataError()

    /** ErrorCode 15 – Выбран не существующий пользователь */
    data class UserNotExist(override val message: String?) : DataError()

    /** ErrorCode 16 – У пользователя нет прав управлять другим пользователем */
    data class NoPermissionToManageUser(override val message: String?) : DataError()

    /** ErrorCode 203 – Невозможно удалить класс: в нём есть ученики */
    data class CannotDeleteClassWithStudents(override val message: String?) : DataError()

    /** ErrorCode 204 – Невозможно удалить класс: для него запланированы уроки */
    data class CannotDeleteClassWithLessons(override val message: String?) : DataError()

    /** ErrorCode 207 – Кабинет не принадлежит школе класса */
    data class RoomNotBelongsToSchool(override val message: String?) : DataError()

    /** ErrorCode 208 – У учителя уже есть урок в это время */
    data class TeacherTimeConflict(override val message: String?) : DataError()

    /** ErrorCode 209 – В классе уже есть урок в это время */
    data class ClassTimeConflict(override val message: String?) : DataError()

    /** ErrorCode 210 – Время начала должно быть раньше времени окончания */
    data class StartTimeAfterEndTime(override val message: String?) : DataError()

    /** ErrorCode 211 – Невозможно удалить школу: в ней есть классы */
    data class CannotDeleteSchoolWithClasses(override val message: String?) : DataError()

    /** ErrorCode 212 – Нельзя удалить кабинет: он используется в уроках */
    data class CannotDeleteRoomUsedInLessons(override val message: String?) : DataError()

    // ========== Ресурс не найден (HTTP 404) ==========
    /** ErrorCode 100 – Запись не найдена (общий случай) */
    data class RecordNotFound(override val message: String?) : DataError()

    /** ErrorCode 101 – Школа с указанным ID не найдена */
    data class SchoolNotFound(override val message: String?) : DataError()

    /** ErrorCode 103 – Класс не найден */
    data class ClassNotFound(override val message: String?) : DataError()

    /** ErrorCode 104 – Предмет не найден */
    data class SubjectNotFound(override val message: String?) : DataError()

    /** ErrorCode 105 – Учитель не найден */
    data class TeacherNotFound(override val message: String?) : DataError()

    // ========== Конфликт (HTTP 409) ==========
    /** ErrorCode 200 – Нарушение уникальности (дубликат) */
    data class UniqueViolation(override val message: String?) : DataError()

    /** ErrorCode 201 – Нарушение внешнего ключа */
    data class ForeignKeyViolation(override val message: String?) : DataError()

    /** ErrorCode 202 – Запись уже существует */
    data class RecordAlreadyExists(override val message: String?) : DataError()

    // ========== Ошибки сервера (HTTP 500) ==========
    /** ErrorCode 998 – Внутренняя ошибка сервера */
    data class InternalServerError(override val message: String?) : DataError()

    /**
     * Ошибка при работе c файловой системой
     */
    data class FileError(override val cause: Throwable?) :
        DataError()
}