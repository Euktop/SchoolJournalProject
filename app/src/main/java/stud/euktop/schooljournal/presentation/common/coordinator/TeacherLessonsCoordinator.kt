package stud.euktop.schooljournal.presentation.common.coordinator

import stud.euktop.domain.model.lesson.LessonFilter
import stud.euktop.domain.repository.LessonRepository
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.main.teacher.teacherLessons.TeacherLessonItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherLessonsCoordinator @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val userRepository: UserAdminRepository,
    private val coordinatorExec: CoordinatorExec
) {

    suspend fun getLessons(
        classId: Int,
        subjectId: Int,
        dateFrom: java.util.Date? = null,
        dateTo: java.util.Date? = null
    ): CoordinatorResult<List<TeacherLessonItem>> = coordinatorExec.exec {
        runCatching {
            val filter = LessonFilter(
                classId = classId,
                subjectId = subjectId,
                dateFrom = dateFrom,
                dateTo = dateTo
            )
            val lessons = lessonRepository.getLessons(filter).getOrThrow()
            lessons.map { lesson ->
                val teacher = userRepository.getUser(lesson.teacher.userId).getOrNull()
                TeacherLessonItem(
                    lessonId = lesson.lessonId,
                    date = lesson.date,
                    topic = lesson.topic,
                    startTime = lesson.startTime,
                    endTime = lesson.endTime,
                    roomName = lesson.room?.name,
                    teacherName = teacher?.let { "${it.lastName} ${it.firstName}" } ?: ""
                )
            }
        }
    }
}