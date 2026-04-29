package stud.euktop.schooljournal.presentation.common.utils

import stud.euktop.domain.model.Gender
import stud.euktop.schooljournal.R
import stud.euktop.domain.model.AbsenceTypes as t1
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
    Gender.WOMAN -> R.string.woman
    Gender.NONE -> R.string.none
}