package stud.euktop.schooljournal.presentation.common.filter.lesson

import stud.euktop.domain.model.lesson.LessonFilter
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserProfile
import java.util.Date

data class AppLessonFilter(
    val classInfo: ClassInfo? = null,
    val subjectInfo: Subject? = null,
    val teacherInfo: UserProfile? = null,
    val dateFrom: Date? = null,
    val dateTo: Date? = null
) {
    fun toDomain(): LessonFilter = LessonFilter(
        classId = classInfo?.classId,
        subjectId = subjectInfo?.subjectId,
        teacherId = teacherInfo?.userId,
        dateFrom = dateFrom,
        dateTo = dateTo
    )
}