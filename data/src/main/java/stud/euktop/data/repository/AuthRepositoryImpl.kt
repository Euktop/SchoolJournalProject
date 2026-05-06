// AuthRepositoryImpl.kt
package stud.euktop.data.repository

import com.schooljournal.api.AuthorizationApi
import com.schooljournal.api.UsersApi
import com.schooljournal.model.LoginRequest
import com.schooljournal.model.RegisterRequest
import stud.euktop.data.local.storage.contract.TokenStorage
import stud.euktop.data.local.storage.contract.UserIdStorage
import stud.euktop.data.map.*
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthorizationApi,
    private val usersApi: UsersApi,
    private val tokenStorage: TokenStorage,
    private val userIdStorage: UserIdStorage,
    private val errorHandler: ApiErrorHandler
) : AuthRepository {

    override suspend fun login(login: String, passwordHash: String): Result<UserInfo> =
        errorHandler.safeApiCall {
            val loginResponse = authApi.apiAuthorizationLoginPost(LoginRequest(login, passwordHash))
            val token = loginResponse.token ?: throw IllegalStateException("Token is null")
            val userId = loginResponse.userId ?: throw IllegalStateException("UserId is null")
            tokenStorage.setToken(token)
            userIdStorage.setUserId(userId)
            val userDto = usersApi.apiUsersMeGet()
            val rolesDto = usersApi.apiUsersMeRolesGet()
            mergeRoles(userDto.toDomain(), rolesDto)
        }

    override suspend fun registration(userInfo: UserInfo, password: String): Result<UserInfo> =
        errorHandler.safeApiCall {
            val request = RegisterRequest(
                lastName = userInfo.lastName,
                firstName = userInfo.firstName,
                surName = userInfo.surName ?: "",
                email = userInfo.email,
                password = password,
                gender = userInfo.gender.toNetwork(),
                birthDay = userInfo.birthday?.toLocalDateTime(),
                phone = userInfo.phone
            )
            val result = authApi.apiAuthorizationRegisterPost(request)
            UserInfo(
                userId = result.userId ?: 0,
                lastName = result.lastName ?: "",
                firstName = result.firstName ?: "",
                surName = result.surName,
                birthday = result.birthDay?.toDate(),
                gender = toGender(result.gender),
                email = result.email ?: "",
                phone = result.phone,
                roles = emptyList(),
                dateRegistration = result.dateRegistration.toDate(),
                accountStatus = AccountStatus.PENDING
            )
        }

    override suspend fun getCurrentUser(): Result<UserInfo> =
        errorHandler.safeApiCall {
            val userDto = usersApi.apiUsersMeGet()
            val rolesDto = usersApi.apiUsersMeRolesGet()
            mergeRoles(userDto.toDomain(), rolesDto)
        }
}