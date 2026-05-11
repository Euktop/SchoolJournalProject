package stud.euktop.schooljournal.presentation.main.teacher.teacherLessons

import java.util.Date

data class TeacherLessonItem(
    val lessonId: Int,
    val date: Date,
    val topic: String?,
    val startTime: String,
    val endTime: String,
    val roomName: String?,
    val teacherName: String
)