package stud.euktop.domain.model.lesson

import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.ClassInfoFilter
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.RoomFilter
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.model.user.UserInfoFilter
import java.util.Date

data class LessonFilter2(
    val lessonId: Int? = null,
    val classId: Int? = null,
    val subjectId: Int? = null,
    val teacherId: Int? = null,
    val roomId: Int? = null,
    val dateFrom: Date? = null,
    val dateTo: Date? = null
)