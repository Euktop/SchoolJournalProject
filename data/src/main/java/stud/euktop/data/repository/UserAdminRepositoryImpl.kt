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
                filterByRoleId = filter.role != null,
                schoolId = filter.schoolId,
                filterBySchoolId = filter.schoolId != null,
                accountStatusId = filter.accountStatus?.id,
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
                roleId = profile.roles.firstOrNull()?.role?.toNetwork(),
                schoolId = profile.roles.firstOrNull()?.schoolId
            )
            val resultId = usersApi.apiUsersAdminPost(request)

            // Добавляем дополнительные роли, если их несколько
            profile.roles.forEach {
                addUserRole(resultId, it.role, it.schoolId).getOrThrow()
            }
            getUser(resultId).getOrThrow()
        }

    override suspend fun updateUser(update: UserUpdate): Result<UserProfile> =
        errorHandler.safeApiCall {
            usersApi.apiUsersIdPatch(
                id = update.userId,
                lastName = update.lastName.uValue,
                lastNameUpdate = update.lastName.isUpdate,
                firstName = update.firstName.uValue,
                firstNameUpdate = update.firstName.isUpdate,
                surName = update.surName.uValue,
                surNameUpdate = update.surName.isUpdate,
                gender = update.gender.uValue?.toNetwork(),
                genderUpdate = update.gender.isUpdate,
                birthDay = update.birthday.uValue?.toLocalDateTime(),
                birthDayUpdate = update.birthday.isUpdate,
                email = update.email.uValue,
                emailUpdate = update.email.isUpdate,
                phone = update.phone.uValue,
                phoneUpdate = update.phone.isUpdate
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