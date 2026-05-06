package stud.euktop.data.map

import com.schooljournal.model.GenderType
import stud.euktop.domain.model.user.Gender

fun GenderType?.toDomain() = when (this) {
    GenderType.M -> Gender.MALE
    GenderType.F -> Gender.WOMAN
    else -> Gender.NONE
}

fun Gender?.toNetwork() = when (this) {
    Gender.MALE -> GenderType.M
    Gender.WOMAN -> GenderType.F
    else -> GenderType.N
}