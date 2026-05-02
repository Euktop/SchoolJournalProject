package stud.euktop.domain.model.homework

import stud.euktop.domain.model.common.DateDiff
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.model.user.UserInfo

data class HomeworkFilter(
    val lesson: Lesson? = null,
    val subject: Subject? = null,
    val description: String? = null,
    val createdDiff: DateDiff = DateDiff(),
    val createdByUser: UserInfo? = null,
    val subjectFilter: SubjectFilter = SubjectFilter(),
)