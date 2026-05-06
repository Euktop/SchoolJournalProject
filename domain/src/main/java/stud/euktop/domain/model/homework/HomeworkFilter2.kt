package stud.euktop.domain.model.homework

import java.util.Date

data class HomeworkFilter2(
    val homeworkId: Int? = null,
    val lessonId: Int? = null,
    val teacherId: Int? = null,
    val studentId: Int? = null,
    val classId: Int? = null,
    val subjectId: Int? = null,
    val description: String? = null,
    val createdFrom: Date? = null,
    val createdTo: Date? = null
)