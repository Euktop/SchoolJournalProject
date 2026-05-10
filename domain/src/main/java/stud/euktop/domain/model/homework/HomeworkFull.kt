package stud.euktop.domain.model.homework

import stud.euktop.domain.model.lesson.LessonFull
import stud.euktop.domain.model.user.UserRef
import java.util.Date

data class HomeworkFull(
    val homeworkId: Int,
    val lesson: LessonFull,
    val description: String,
    val createdAt: Date,
    val createdBy: UserRef,
    val media: List<HomeworkMedia> = emptyList()
)