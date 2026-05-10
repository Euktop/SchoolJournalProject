package stud.euktop.data.map

import com.schooljournal.model.GenderType
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Gender

fun GenderType?.toDomain() = when (this) {
    GenderType.M -> Gender.MALE
    GenderType.F -> Gender.FEMALE
    else -> Gender.NONE
}

fun Gender?.toNetwork() = when (this) {
    Gender.MALE -> GenderType.M
    Gender.FEMALE -> GenderType.F
    else -> GenderType.N
}

fun AccountStatus.toNetwork() = when (this) {
    AccountStatus.ACTIVE -> 0
    AccountStatus.DELETED -> 1
    AccountStatus.PENDING -> 2
    AccountStatus.BLOCKED -> 3
}

fun Int.toAccountStatus() = when (this) {
    0 -> AccountStatus.ACTIVE
    1 -> AccountStatus.DELETED
    2 -> AccountStatus.PENDING
    3 -> AccountStatus.BLOCKED
    else -> null
}