package stud.euktop.domain.model.lesson

import stud.euktop.domain.model.common.Pagination
import java.util.Date

data class LessonFilter(
    val lessonId: Int? = null,
    val classId: Int? = null,
    val subjectId: Int? = null,
    val teacherId: Int? = null,
    val roomId: Int? = null,
    val dateFrom: Date? = null,
    val dateTo: Date? = null,
    val pagination: Pagination = Pagination()
)