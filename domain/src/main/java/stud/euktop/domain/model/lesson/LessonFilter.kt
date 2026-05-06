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

data class LessonFilter(
    val classInfo: ClassInfo? = null,
    val classInfoFilter: ClassInfoFilter = ClassInfoFilter(),
    val subject: Subject? = null,
    val subjectFilter: SubjectFilter = SubjectFilter(),
    val teacher: UserInfo? = null,
    val teacherFilter: UserInfoFilter = UserInfoFilter(),
    val date: Date? = null,
    val room: Room? = null,
    val roomFilter: RoomFilter = RoomFilter(),
) {
    /*    companion object{
            fun exec(lesson: Lesson?) =if (lesson==null)null else
                LessonFilter(
                    classInfo = ClassInfoFilter.exec(lesson.classInfo),
                    subject = SubjectFilter.exec(lesson.subject),
                    teacher = UserInfoFilter.exec(lesson.teacher),
                    date = (lesson.date),
                    room = RoomFilter.exec(lesson.room),
                    locationAddress = (lesson.locationAddress),
                )
        }*/
}