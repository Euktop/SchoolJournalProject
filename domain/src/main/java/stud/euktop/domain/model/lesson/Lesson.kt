package stud.euktop.domain.model.lesson

import java.util.Date

data class Lesson(
    val lessonId: Int = 0,
    val classId: Int,
    val subjectId: Int,
    val teacherId: Int,
    val date: Date,
    val topic: String?,
    val startTime: String,
    val endTime: String,
    val roomId: Int?,
    val locationAddress: String?
)
