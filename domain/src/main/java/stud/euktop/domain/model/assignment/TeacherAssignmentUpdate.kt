package stud.euktop.domain.model.assignment

import stud.euktop.domain.model.common.Field
import java.util.Date

data class TeacherAssignmentUpdate(
    val id: AssignmentId,
    val validToDate: Field<Date?> = Field(),
    val isPrimary: Field<Boolean> = Field(),
    val comment: Field<String> = Field()
)