package stud.euktop.schooljournal.presentation.auth.common.contract

import stud.euktop.domain.model.Gender
import stud.euktop.domain.model.Profile
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import java.util.Date

/**
 * Координатор для операций аутентификации.
 * Определяет методы для входа, регистрации, выхода и проверки профиля.
 */
interface AuthCoordinator {

    /**
     * Выполняет вход пользователя.
     *
     * @param email Email пользователя.
     * @param password Пароль.
     * @return CoordinatorResult с данными пользователя и, возможно, следующим экраном.
     */
    suspend fun login(email: String, password: String): CoordinatorResult<Profile>

    /**
     * Сохраняет временное состояние профиля
     */
    suspend fun saveProfile(
        lastName: String,
        firstName: String,
        surName: String,
        gender: Gender,
        birthDay: Date,
        email: String,
        phone: String?,
    )

    /**
     * Выполняет регистрацию нового пользователя.
     *
     * @param password Пароль.
     * @return CoordinatorResult с данными пользователя и следующим экраном.
     */
    suspend fun register(password: String): CoordinatorResult<Profile>

    /**
     * Выполняет выход из системы.
     *
     * @return CoordinatorResult с Unit и следующим экраном (логин).
     */
    suspend fun logout(): CoordinatorResult<Unit>

    /**
     * Проверяет наличие профиля у текущего пользователя.
     *
     * @return UserEntity, если профиль существует и заполнен, иначе null.
     */
    suspend fun getUser(): CoordinatorResult<Profile>
}