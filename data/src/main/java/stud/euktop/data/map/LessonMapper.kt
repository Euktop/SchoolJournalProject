// data/src/main/java/stud/euktop/data/map/LessonMapper.kt
package stud.euktop.data.map

import com.schooljournal.model.CreateLessonRequest
import com.schooljournal.model.GetLessonByIdResult
import com.schooljournal.model.GetLessonsResult
import com.schooljournal.model.UpdateLessonResult
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.lesson.LessonFull
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserRef
import java.time.LocalDateTime
import java.util.Date

// DTO списка -> Lesson
private fun toLesson(
    lessonId: Int?,
    classId: Int?,
    subjectId: Int?,
    teacherId: Int?,
    date: LocalDateTime?,
    topic: String?,
    startTime: String?,
    endTime: String?,
    roomId: Int?,
    locationAddress: String?,
) = Lesson(
    lessonId = lessonId ?: 0,
    classId = classId ?: 0,
    subjectId = subjectId ?: 0,
    teacherId = teacherId ?: 0,
    date = date?.toDate() ?: Date(),
    topic = topic,
    startTime = startTime ?: "",
    endTime = endTime ?: "",
    roomId = roomId,
    locationAddress = locationAddress
)

private fun toLessonFull(
    lessonId: Int?,
    classId: Int?,
    classGrade: Int?,
    classLetter: String?,
    teacherId: Int?,
    subjectId: Int?,
    subjectName: String?,
    teacherLastName: String?,
    teacherFirstName: String?,
    teacherSurName: String?,
    date: LocalDateTime?,
    topic: String?,
    startTime: String?,
    endTime: String?,
    roomId: Int?,
    roomName: String?,
    locationAddress: String?
) = LessonFull.createObject(
    lessonId = lessonId,
    classInfo = ClassInfo.createObject(
        classId = classId,
        schoolId = null,
        grade = classGrade,
        letter = classLetter,
        academicYearStart = null,
        academicYearEnd = null,
        teacherId = teacherId
    ),
    subject = Subject.createObject(
        subjectId = subjectId,
        name = subjectName,
        description = null
    ),
    teacher = UserRef.createObject(
        userId = teacherId,
        lastName = teacherLastName,
        firstName = teacherFirstName,
        surName = teacherSurName
    ),
    date = date?.toDate(),
    topic = topic,
    startTime = startTime,
    endTime = endTime,
    room = Room.createObject(
        roomId = roomId,
        schoolId = null,
        name = roomName
    ),
    locationAddress = locationAddress
)

internal fun GetLessonsResult.toLessonFull() = toLessonFull(
    lessonId = lessonId,
    classId = classId,
    classGrade = classGrade,
    classLetter = classLetter,
    teacherId = teacherId,
    subjectId = subjectId,
    subjectName = subjectName,
    teacherLastName = teacherLastName,
    teacherFirstName = teacherFirstName,
    teacherSurName = teacherSurName,
    date = date,
    topic = topic,
    startTime = startTime,
    endTime = endTime,
    roomId = roomId,
    roomName = roomName,
    locationAddress = locationAddress
)

internal fun UpdateLessonResult.toDomain(): Lesson = toLesson(
    lessonId = lessonId,
    classId = classId,
    subjectId = subjectId,
    teacherId = teacherId,
    date = date,
    topic = topic,
    startTime = startTime,
    endTime = endTime,
    roomId = roomId,
    locationAddress = locationAddress
)

internal fun GetLessonByIdResult.toDomain() = toLessonFull(
    lessonId = lessonId,
    classId = classId,
    classGrade = classGrade,
    classLetter = classLetter,
    teacherId = teacherId,
    subjectId = subjectId,
    subjectName = subjectName,
    teacherLastName = teacherLastName,
    teacherFirstName = teacherFirstName,
    teacherSurName = teacherSurName,
    date = date,
    topic = topic,
    startTime = startTime,
    endTime = endTime,
    roomId = roomId,
    roomName = roomName,
    locationAddress = locationAddress
)

internal fun Lesson.toCreateRequest(): CreateLessonRequest = CreateLessonRequest(
    classId = classId,
    subjectId = subjectId,
    teacherId = teacherId,
    date = date.toLocalDateTime(),
    topic = topic,
    startTime = startTime,
    endTime = endTime,
    roomId = roomId,
    locationAddress = locationAddress
)