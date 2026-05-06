package stud.euktop.domain.model.homework

import stud.euktop.domain.model.common.DateDiff
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.lesson.LessonFilter
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.model.user.UserInfoFilter

data class HomeworkFilter(
    val lesson: Lesson? = null,
    val lessonFilter: LessonFilter = LessonFilter(),
    val subject: Subject? = null,
    val subjectFilter: SubjectFilter = SubjectFilter(),
    val description: String? = null,
    val createdDiff: DateDiff = DateDiff(),
    val createdByUser: UserInfo? = null,
    val createdByUserFilter: UserInfoFilter = UserInfoFilter(),
)