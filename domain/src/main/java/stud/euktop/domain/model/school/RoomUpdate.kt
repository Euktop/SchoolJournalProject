package stud.euktop.domain.model.school

import stud.euktop.domain.model.common.Field

data class RoomUpdate(
    val roomId: Int,
    val schoolId: Field<Int> = Field(),
    val name: Field<String> = Field()
)