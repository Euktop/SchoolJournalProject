package stud.euktop.domain.model.schedule

import java.util.Date

data class StudentScheduleItem(
    val lessonId: Int,
    val date: Date,
    val startTime: String,
    val endTime: String,
    val topic: String,
    val subjectName: String,
    val teacherLastName: String?,
    val teacherFirstName: String?,
    val teacherSurName: String?,
    val roomName: String?,
    val locationAddress: String?
) {
    val teacherFullName
        get() = "$teacherLastName $teacherFirstName $teacherSurName"
}