package stud.euktop.domain.model.lesson

import java.util.Date

/**
 * Упрощённая информация об уроке для списка учителя.
 */
data class TeacherLessonItem(
    val lessonId: Int,
    val date: Date,
    val topic: String?,
    val startTime: String,   // "HH:mm"
    val endTime: String,     // "HH:mm"
    val roomName: String?,
    val teacherName: String
)