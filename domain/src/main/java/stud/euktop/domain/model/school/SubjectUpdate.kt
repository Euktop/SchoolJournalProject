package stud.euktop.domain.model.school

import stud.euktop.domain.model.common.Field

data class SubjectUpdate(
    val subjectId: Int,
    val name: Field<String> = Field(),
    val description: Field<String> = Field()
)