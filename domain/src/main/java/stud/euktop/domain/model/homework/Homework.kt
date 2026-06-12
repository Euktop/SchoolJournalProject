package stud.euktop.domain.model.homework

import java.util.Date

data class Homework(
    val homeworkId: Int = 0,
    val lessonId: Int,
    val description: String,
    val createdAt: Date,
    val medias: List<HomeworkMedia>,
    val createdByUserId: Int,
    val isSubmitted: Boolean = false
)
