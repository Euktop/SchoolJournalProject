package stud.euktop.domain.model

data class TeacherLessonItem(
    val lessonId: Int,
    val date: String,          // "28.04.2026"
    val topic: String?,
    val startTime: String,     // "08:30"
    val endTime: String,       // "09:15"
    val roomName: String?,
    val teacherName: String
)
