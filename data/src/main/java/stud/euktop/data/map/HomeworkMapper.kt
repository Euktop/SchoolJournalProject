// data/src/main/java/stud/euktop/data/map/HomeworkMapper.kt
package stud.euktop.data.map

import com.schooljournal.model.AddMediaToHomeworkResult
import com.schooljournal.model.CreateHomeworkRequest
import com.schooljournal.model.GetHomeworkByIdResult
import com.schooljournal.model.GetHomeworkMediaResult
import com.schooljournal.model.GetHomeworksResult
import com.schooljournal.model.UpdateHomeworkRequest
import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.homework.HomeworkFull
import stud.euktop.domain.model.homework.HomeworkMedia
import stud.euktop.domain.model.homework.HomeworkUpdate
import stud.euktop.domain.model.lesson.LessonFull
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserRef
import java.util.Date

internal fun GetHomeworkByIdResult.toHomework(): Homework = Homework(
    homeworkId = homeworkId ?: 0,
    lessonId = lessonId ?: 0,
    description = description ?: "",
    createdAt = createAt?.toDate() ?: Date(),
    medias = emptyList(),
    createdByUserId = 0
)

internal fun GetHomeworksResult.toHomework(): Homework = Homework(
    homeworkId = homeworkId ?: 0,
    lessonId = lessonId ?: 0,
    description = description ?: "",
    createdAt = createdAt?.toDate() ?: Date(),
    medias = emptyList(),
    createdByUserId = 0
)

// Преобразование DTO -> HomeworkFull (с вложенным уроком)
internal fun GetHomeworkByIdResult.toHomeworkFull(): HomeworkFull = HomeworkFull(
    homeworkId = homeworkId ?: 0,
    lesson = LessonFull(
        lessonId = lessonId ?: 0,
        classInfo = ClassInfo(
            classId = classId ?: 0,
            schoolId = 0,
            grade = classGrade ?: 0,
            letter = classLetter ?: "",
            academicYearStart = -1,
            academicYearEnd = -1,
            teacherId = 0
        ),
        subject = Subject(
            subjectId = 0,
            name = subjectName ?: "",
            description = null
        ),
        teacher = UserRef(
            userId = 0,
            lastName = teacherLastName ?: "",
            firstName = teacherFirstName ?: "",
            surName = teacherSurName
        ),
        date = lessonDate?.toDate() ?: Date(),
        topic = lessonTopic ?: "",
        startTime = "",
        endTime = "",
        room = null,
        locationAddress = null
    ),
    description = description ?: "",
    createdAt = createAt?.toDate() ?: Date(),
    createdBy = UserRef(
        userId = 0,
        lastName = "",
        firstName = "",
        surName = null
    ),
    media = emptyList()
)

// Остальные мапперы без изменений
internal fun GetHomeworkMediaResult.toDomain(): HomeworkMedia = HomeworkMedia(
    mediaId = mediaId ?: 0,
    fileName = fileName ?: "",
    contentType = contentType ?: "",
    fileSize = fileSize ?: 0,
    uploadedAt = uploadedAt?.toDate() ?: Date()
)

internal fun Homework.toCreateRequest(): CreateHomeworkRequest = CreateHomeworkRequest(
    lessonId = lessonId,
    description = description
)

internal fun HomeworkUpdate.toUpdateRequest(): UpdateHomeworkRequest = UpdateHomeworkRequest(
    description = description.uValue
)

internal fun AddMediaToHomeworkResult.toDomain(): HomeworkMedia = HomeworkMedia(
    mediaId = mediaId ?: 0,
    fileName = fileName ?: "",
    contentType = contentType ?: "",
    fileSize = fileSize ?: 0,
    uploadedAt = uploadedAt?.toDate() ?: Date()
)