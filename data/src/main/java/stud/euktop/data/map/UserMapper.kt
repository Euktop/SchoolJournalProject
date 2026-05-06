package stud.euktop.data.map

import com.schooljournal.model.*
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.*
import java.util.Date

internal fun toGender(gender: String?) = when (gender) {
    "M" -> Gender.MALE
    "F" -> Gender.WOMAN
    else -> Gender.NONE
}

internal fun toAccountStatus(id: Int?) = AccountStatus.entries.getOrElse(
    id?.minus(1) ?: 0
) { AccountStatus.ACTIVE }

internal fun GetUserByIdResult.toDomain() = UserInfo(
    userId = userId ?: 0,
    lastName = lastName ?: "",
    firstName = firstName ?: "",
    surName = surName,
    birthday = birthDay?.toDate(),
    gender = toGender(gender),
    email = email ?: "",
    phone = phone,
    roles = emptyList(),
    dateRegistration = dateRegistration.toDate(),
    accountStatus = toAccountStatus(accountStatusId)
)

internal fun GetUsersResult.toDomain() = UserInfo(
    userId = userId ?: 0,
    lastName = lastName ?: "",
    firstName = firstName ?: "",
    surName = surName,
    birthday = birthDay?.toDate(),
    gender = toGender(gender),
    email = email ?: "",
    phone = phone,
    roles = emptyList(),
    dateRegistration = dateRegistration.toDate(),
    accountStatus = toAccountStatus(accountStatusId),
)

internal fun mergeRoles(user: UserInfo, roleDtos: List<GetUserRolesResult>): UserInfo {
    val roles = roleDtos.mapNotNull { dto ->
        val role = Role.entries.getOrNull(dto.roleId?.minus(1) ?: return@mapNotNull null)
        RoleSchools(role ?: return@mapNotNull null, dto.schoolId?.let { School(schoolId = it) })
    }
    return user.copy(roles = roles)
}
