package stud.euktop.data.map

import com.schooljournal.model.AddUserRoleResult
import com.schooljournal.model.GetCurrentUserInfoResult
import com.schooljournal.model.GetUserByIdResult
import com.schooljournal.model.GetUserRolesResult
import com.schooljournal.model.GetUsersResult
import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.model.common.DataError
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Gender
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.model.user.UserRole
import java.time.LocalDateTime
import java.util.Date

internal fun String?.toGender() = when (this) {
    "M" -> Gender.MALE
    "F" -> Gender.FEMALE
    else -> Gender.NONE
}

internal fun Int?.toAccountStatus(): AccountStatus = when (this) {
    1 -> AccountStatus.ACTIVE
    2 -> AccountStatus.DELETED
    3 -> AccountStatus.PENDING
    4 -> AccountStatus.BLOCKED
    else -> AccountStatus.PENDING
}

private fun toUserProfileFunc(
    roles: List<UserRole>,
    userId: Int? = null,
    lastName: String? = null,
    firstName: String? = null,
    surName: String? = null,
    email: String? = null,
    phone: String? = null,
    gender: String? = null,
    birthDay: LocalDateTime? = null,
    dateRegistration: LocalDateTime? = null,
    accountStatusId: Int? = null
) = UserProfile(
    userId = userId ?: 0,
    lastName = lastName ?: "",
    firstName = firstName ?: "",
    surName = surName,
    birthday = birthDay?.toDate(),
    gender = gender.toGender(),
    email = email ?: "",
    phone = phone,
    roles = roles,
    dateRegistration = dateRegistration.toDate(),
    accountStatus = accountStatusId.toAccountStatus()
)

internal fun GetUsersResult.toDomain(roles: List<UserRole>) = toUserProfileFunc(
    roles = roles,
    userId = userId,
    lastName = lastName,
    firstName = firstName,
    surName = surName,
    email = email,
    phone = phone,
    gender = gender,
    birthDay = birthDay,
    dateRegistration = dateRegistration,
    accountStatusId = accountStatusId
)

private val rolesMap = mapOf(
    Role.ADMIN to 0,
    Role.DIRECTOR to 1,
    Role.TEACHER to 2,
    Role.STUDENT to 3,
    Role.PARENT to 4,
)

internal fun Int?.toRole(): Role = rolesMap.entries.firstOrNull { it.value == this }?.key
    ?: throw DataError.Unknown("Unsupported role for this method")


internal fun Role.toNetwork(): Int = rolesMap[this] ?: throw DataError.Unknown(null)

internal fun String.toAbsenceType(): AbsenceTypes = when (this) {
    "н" -> AbsenceTypes.IRRESPECTABLE
    "б" -> AbsenceTypes.ILL
    "у" -> AbsenceTypes.RESPECTABLE
    "2" -> AbsenceTypes.G2
    "3" -> AbsenceTypes.G3
    "4" -> AbsenceTypes.G4
    "5" -> AbsenceTypes.G5
    else -> AbsenceTypes.IRRESPECTABLE
}

internal fun GetUsersResult.toUserListItem(): UserListItem = UserListItem(
    userId = userId ?: 0,
    lastName = lastName ?: "",
    firstName = firstName ?: "",
    surName = surName,
    email = email ?: "",
    accountStatus = accountStatusId.toAccountStatus()
)

internal fun AddUserRoleResult.toDomain(): UserRole = UserRole(
    role = roleId.toRole(),
    schoolId = schoolId,
    assignedAt = assignedAt.toDate()
)

// в UserMapper.kt (добавьте/замените эти функции)

internal fun mergeRoles(user: UserProfile, roleDtos: List<GetUserRolesResult>): UserProfile {
    val roles = roleDtos.mapNotNull { dto ->
        val role = dto.roleId?.toRole() ?: return@mapNotNull null
        UserRole(role, dto.schoolId, dto.assignedAt?.toDate() ?: Date())
    }
    return user.copy(roles = roles)
}

internal fun GetUserRolesResult.toDomain() = UserRole(
    role = roleId.toRole(),
    schoolId = schoolId,
    assignedAt = assignedAt.toDate()
)

internal fun GetCurrentUserInfoResult.toDomain(roles: List<UserRole>) = toUserProfileFunc(
    roles = roles,
    userId = userId,
    lastName = lastName,
    firstName = firstName,
    surName = surName,
    email = email,
    phone = phone,
    gender = gender,
    birthDay = birthDay,
    dateRegistration = dateRegistration,
    accountStatusId = AccountStatus.ACTIVE.toNetwork()
)

internal fun GetUserByIdResult.toUserProfile(roleDtos: List<GetUserRolesResult>): UserProfile {
    val roles = roleDtos.mapNotNull { dto ->
        val roleId = dto.roleId ?: return@mapNotNull null
        UserRole(
            role = roleId.toRole(),
            schoolId = dto.schoolId,
            assignedAt = dto.assignedAt?.toDate() ?: Date()
        )
    }
    return toUserProfileFunc(
        roles = roles,
        userId = userId,
        lastName = lastName,
        firstName = firstName,
        surName = surName,
        email = email,
        phone = phone,
        gender = gender,
        birthDay = birthDay,
        dateRegistration = dateRegistration,
        accountStatusId = accountStatusId
    )
}