package stud.euktop.data.repository

import com.schooljournal.api.AuthorizationApi
import com.schooljournal.api.UsersApi
import com.schooljournal.model.LoginRequest
import com.schooljournal.model.RegisterRequest
import stud.euktop.data.local.storage.contract.RoleStorage
import stud.euktop.data.local.storage.contract.TokenStorage
import stud.euktop.data.local.storage.contract.UserIdStorage
import stud.euktop.data.map.toDomain
import stud.euktop.data.map.toLocalDateTime
import stud.euktop.data.map.toNetwork
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthorizationApi,
    private val usersApi: UsersApi,
    private val tokenStorage: TokenStorage,
    private val userIdStorage: UserIdStorage,
    private val roleStorage: RoleStorage,
    private val errorHandler: ApiErrorHandler
) : AuthRepository {

    override suspend fun login(login: String, passwordHash: String): Result<Unit> =
        errorHandler.safeApiCall {
            val response = authApi.apiAuthorizationLoginPost(LoginRequest(login, passwordHash))
            val token = response.token ?: throw IllegalStateException("No token")
            val userId = response.userId ?: throw IllegalStateException("No userId")

            tokenStorage.setToken(token)
            userIdStorage.setUserId(userId)
            try {
                logger?.d(this.toSimpleTag(), "login", "saved userId=$userId token=${token.takeIf { it.isNotEmpty() } != null}")
            } catch (_: Throwable) {
            }

            val roleIds = response.roles?.map { it.roleId ?: 0 } ?: emptyList()
            // Сохраняем первую роль как основную для простоты, или обрабатываем логику выбора
            roleIds.firstOrNull()?.let { roleStorage.saveRole(Role.entries.getOrNull(it)) }

            Unit
        }

    override suspend fun registration(profile: UserProfile, password: String): Result<Unit> =
        errorHandler.safeApiCall {
            val request = RegisterRequest(
                lastName = profile.lastName,
                firstName = profile.firstName,
                surName = profile.surName ?: "",
                email = profile.email,
                password = password,
                gender = profile.gender.toNetwork(),
                birthDay = profile.birthday?.toLocalDateTime(),
                phone = profile.phone
            )
            authApi.apiAuthorizationRegisterPost(request)
            login(profile.email, password).getOrThrow()
        }

    override suspend fun getCurrentUser(): Result<UserProfile> =
        errorHandler.safeApiCall {
            val roles = usersApi.apiUsersMeRolesGet().map { it.toDomain() }
            usersApi.apiUsersMeGet().toDomain(roles)
        }

    override suspend fun logout(): Result<Unit> {
        tokenStorage.clearAll()
        userIdStorage.clearAll()
        roleStorage.clearAll()
        return Result.success(Unit)
        // Примечание: Если на бэкенде есть эндпоинт logout, его нужно вызвать здесь перед очисткой
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> =
        errorHandler.safeApiCall {
            val userId = userIdStorage.getUserId() ?: throw IllegalStateException("No user logged in")
            usersApi.apiUsersIdPasswordPatch(userId, oldPassword, newPassword)
        }

    override suspend fun getRoles(): Result<List<Role>> {
        return getCurrentUser().fold(
            onSuccess = { Result.success(it.roles.map { role -> role.role }.distinct()) },
            onFailure = { Result.failure(it) }
        )
    }

    override suspend fun saveRole(role: Role): Result<Unit> =
        errorHandler.safeApiCall {
            roleStorage.saveRole(role)
        }

    override suspend fun getSaveRole(): Result<Role?> =
        errorHandler.safeApiCall {
            roleStorage.getRole()
        }
}