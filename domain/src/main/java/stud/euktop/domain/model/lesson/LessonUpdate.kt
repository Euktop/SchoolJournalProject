package stud.euktop.domain.model.lesson

import stud.euktop.domain.model.common.Field
import java.util.Date

data class LessonUpdate(
    val lessonId: Int,
    val classId: Field<Int> = Field(),
    val subjectId: Field<Int> = Field(),
    val teacherId: Field<Int> = Field(),
    val date: Field<Date> = Field(),
    val topic: Field<String?> = Field(),
    val startTime: Field<String> = Field(),
    val endTime: Field<String> = Field(),
    val roomId: Field<Int?> = Field(),
    val locationAddress: Field<String?> = Field()
)