package stud.euktop.domain.model.lesson

import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserRef
import java.util.Date

data class LessonFull(
    val lessonId: Int,
    val classInfo: ClassInfo,
    val subject: Subject,
    val teacher: UserRef,
    val date: Date,
    val topic: String,
    val startTime: String,
    val endTime: String,
    val room: Room?,
    val locationAddress: String?
) {
    fun toLesson() = Lesson(
        lessonId = lessonId,
        classId = classInfo.classId,
        subjectId = subject.subjectId,
        teacherId = teacher.userId,
        date = date,
        topic = topic,
        startTime = startTime,
        endTime = endTime,
        roomId = room?.roomId,
        locationAddress = locationAddress
    )

    companion object {
        fun createObject(
            lessonId: Int?,
            classInfo: ClassInfo,
            subject: Subject,
            teacher: UserRef,
            date: Date?,
            topic: String?,
            startTime: String?,
            endTime: String?,
            room: Room,
            locationAddress: String?
        ) = LessonFull(
            lessonId = lessonId ?: 0,
            classInfo = classInfo,
            subject = subject,
            teacher = teacher,
            date = date ?: Date(),
            topic = topic ?: "",
            startTime = startTime ?: "",
            endTime = endTime ?: "",
            room = room,
            locationAddress = locationAddress ?: ""
        )
    }
}