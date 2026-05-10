package stud.euktop.domain.model.homework

import stud.euktop.domain.model.common.Field

data class HomeworkUpdate(
    val homeworkId: Int,
    val description: Field<String> = Field(),
    val attachedFiles: Field<String?> = Field()
)