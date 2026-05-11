package stud.euktop.schooljournal.presentation.common.filter.homework

import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.model.homework.HomeworkFilter
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import java.util.Date

data class AppHomeworkFilter(
    val homeworkId: Int? = null,
    val lessonId: Int? = null,
    val teacherId: Int? = null,
    val studentId: Int? = null,
    val classId: Int? = null,
    val subject: Subject? = null,
    val subjectFilter: SubjectFilter? = null,
    val description: String? = null,
    val createdFrom: Date? = null,
    val createdTo: Date? = null,
    val pagination: Pagination = Pagination()
) {
    fun toDomain() = HomeworkFilter(
        homeworkId = homeworkId,
        lessonId = lessonId,
        teacherId = teacherId,
        studentId = studentId,
        classId = classId,
        subjectId = subject?.subjectId,
        description = description,
        createdFrom = createdFrom,
        createdTo = createdTo,
        pagination = pagination,
    )
}