package stud.euktop.domain.model.lesson

import stud.euktop.domain.model.school.ClassInfoFilter
import stud.euktop.domain.model.school.RoomFilter
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.model.user.UserInfoFilter
import java.util.Date

data class LessonFilter(
    val classInfo: Int? = null,
    val subject: Int? = null,
    val teacher: Int? = null,
    val date: Date? = null,
    val room: Int? = null
){
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