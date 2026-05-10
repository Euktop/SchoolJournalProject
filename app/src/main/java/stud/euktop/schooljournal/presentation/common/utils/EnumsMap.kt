package stud.euktop.schooljournal.presentation.common.utils

import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Gender
import stud.euktop.domain.model.user.Role
import stud.euktop.schooljournal.R
import stud.euktop.domain.model.attendance.AbsenceTypes as t1
import stud.euktop.uikit.components.markPicker.AbsenceTypes as t2

fun t1.toUI(): t2 = absenceTypes.getValue(this)
fun t2.toDomain(): t1 = absenceTypes.entries.first { it.value == this }.key
private val absenceTypes = mutableMapOf(
    t1.IRRESPECTABLE to t2.IRRESPECTABLE,
    t1.ILL to t2.ILL,
    t1.RESPECTABLE to t2.RESPECTABLE,
    t1.G2 to t2.G2,
    t1.G3 to t2.G3,
    t1.G4 to t2.G4,
    t1.G5 to t2.G5,
)

fun Gender.toMessageId() = when (this) {
    Gender.MALE -> R.string.male
    Gender.FEMALE -> R.string.woman
    Gender.NONE -> R.string.none
}

fun Role.toMessageId() = when (this) {
    Role.ADMIN -> R.string.administrator
    Role.DIRECTOR -> R.string.director
    Role.TEACHER -> R.string.teacher
    Role.STUDENT -> R.string.student
    Role.PARENT -> R.string.representative
}

fun AccountStatus.toMessageId() = when (this) {
    AccountStatus.ACTIVE -> R.string.active
    AccountStatus.DELETED -> R.string.deleting
    AccountStatus.PENDING -> R.string.pending
    AccountStatus.BLOCKED -> R.string.blocked
}