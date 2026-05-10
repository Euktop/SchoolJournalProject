package stud.euktop.data.repository

import com.schooljournal.api.RolesApi
import com.schooljournal.api.UsersApi
import com.schooljournal.model.CreateUserRequest
import stud.euktop.data.map.toDomain
import stud.euktop.data.map.toLocalDateTime
import stud.euktop.data.map.toNetwork
import stud.euktop.data.map.toUserListItem
import stud.euktop.data.map.toUserProfile
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.model.user.UserRole
import stud.euktop.domain.model.user.UserUpdate
import stud.euktop.domain.repository.UserAdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAdminRepositoryImpl @Inject constructor(
    private val usersApi: UsersApi,
    private val rolesApi: RolesApi,
    private val errorHandler: ApiErrorHandler
) : UserAdminRepository {

    override suspend fun getUsers(filter: UserFilter): Result<List<UserListItem>> =
        errorHandler.safeApiCall {
            val dtos = usersApi.apiUsersFilterGet(
                fullName = filter.fullName,
                roleId = filter.role?.toNetwork(),
                schoolId = filter.schoolId,
                accountStatusId = filter.accountStatus?.toNetwork(),
                offset = filter.pagination.offset,
                limit = filter.pagination.limit
            )
            dtos.map { it.toUserListItem() }
        }

    override suspend fun getUser(userId: Int): Result<UserProfile> =
        errorHandler.safeApiCall {
            val userDto = usersApi.apiUsersIdGet(userId)
            val rolesDto = rolesApi.apiRolesUserUserIdGet(userId)
            userDto.toUserProfile(rolesDto)
        }

    override suspend fun addUser(profile: UserProfile, password: String?): Result<UserProfile> =
        errorHandler.safeApiCall {
            val request = CreateUserRequest(
                lastName = profile.lastName,
                firstName = profile.firstName,
                surName = profile.surName,
                gender = profile.gender.toNetwork(),
                birthDay = profile.birthday?.toLocalDateTime(),
                email = profile.email,
                phone = profile.phone,
            )
            val result = usersApi.apiUsersAdminPost(request)
            profile.roles.forEach {
                addUserRole(result, it.role, it.schoolId)
            }
            getUser(result).getOrThrow()
        }

    override suspend fun updateUser(update: UserUpdate): Result<UserProfile> =
        errorHandler.safeApiCall {
            usersApi.apiUsersIdPatch(
                id = update.userId,
                lastName = update.lastName.uValue,
                firstName = update.firstName.uValue,
                surName = update.surName.uValue,
                gender = update.gender.uValue?.toNetwork(),
                birthDay = update.birthday.uValue?.toLocalDateTime(),
                email = update.email.uValue,
                phone = update.phone.uValue
            )
            getUser(update.userId).getOrThrow()
        }

    override suspend fun deleteUser(userId: Int): Result<Unit> =
        errorHandler.safeApiCall {
            usersApi.apiUsersIdDelete(userId)
        }

    override suspend fun addUserRole(
        userId: Int,
        role: Role,
        schoolId: Int?
    ): Result<UserRole> = errorHandler.safeApiCall {
        rolesApi.apiRolesUserUserIdRoleRoleIdPost(
            userId = userId,
            roleId = role.toNetwork(),
            schoolId = schoolId
        ).toDomain()
    }

    override suspend fun removeRole(
        userId: Int,
        role: Role,
        schoolId: Int?
    ): Result<Unit> = errorHandler.safeApiCall {
        rolesApi.apiRolesUserUserIdRoleRoleIdDelete(userId, role.toNetwork(), schoolId)
    }
}