package stud.euktop.schooljournal.presentation.common.coordinator

import stud.euktop.domain.model.assignment.TeacherAssignmentFilter
import stud.euktop.domain.model.lesson.LessonFilter
import stud.euktop.domain.repository.AssignmentAdminRepository
import stud.euktop.domain.repository.ClassAdminRepository
import stud.euktop.domain.repository.LessonRepository
import stud.euktop.domain.repository.SubjectAdminRepository
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.main.teacher.dashboard.TeacherStatistics
import stud.euktop.schooljournal.presentation.main.teacher.teacherClass.TeacherClassItem
import stud.euktop.schooljournal.presentation.main.teacher.teacherLessons.TeacherLessonItem
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherCoordinator @Inject constructor(
    private val assignmentRepository: AssignmentAdminRepository,
    private val classRepository: ClassAdminRepository,
    private val subjectRepository: SubjectAdminRepository,
    private val lessonRepository: LessonRepository,
    private val coordinatorExec: CoordinatorExec,
) {

    suspend fun getTeacherClasses(teacherId: Int): CoordinatorResult<List<TeacherClassItem>> =
        coordinatorExec.exec {
            runCatching {
                val assignments = assignmentRepository.getTeacherAssignments(
                    TeacherAssignmentFilter(teacherId = teacherId)
                ).getOrThrow()
                val distinctPairs = assignments.map { it.assignmentId }
                    .distinctBy { Pair(it.classId, it.subjectId) }
                val result = mutableListOf<TeacherClassItem>()
                for (pair in distinctPairs) {
                    val classInfo = classRepository.getClass(pair.classId).getOrThrow()
                    val subject = subjectRepository.getSubject(pair.subjectId).getOrThrow()
                    result.add(
                        TeacherClassItem(
                            classId = pair.classId,
                            grade = classInfo.grade,
                            letter = classInfo.letter,
                            subjectId = pair.subjectId,
                            subjectName = subject.name
                        )
                    )
                }
                result
            }
        }

    suspend fun getNextLesson(teacherId: Int): CoordinatorResult<TeacherLessonItem?> =
        coordinatorExec.exec {
            runCatching {
                val now = Date()
                val endOfDay = Calendar.getInstance().apply {
                    time = now
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                }.time
                val lessons = lessonRepository.getLessons(
                    LessonFilter(teacherId = teacherId, dateFrom = now, dateTo = endOfDay)
                ).getOrNull() ?: emptyList()
                lessons.minByOrNull { it.date }?.let { full ->
                    TeacherLessonItem(
                        lessonId = full.lessonId,
                        date = full.date,
                        topic = full.topic,
                        startTime = full.startTime,
                        endTime = full.endTime,
                        roomName = full.room?.name,
                        teacherName = "${full.teacher.lastName} ${full.teacher.firstName}"
                    )
                }
            }
        }

    private fun startOfDay(): Date = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    private fun endOfDay(): Date = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.time

    suspend fun getTeacherStatistics(teacherId: Int): CoordinatorResult<TeacherStatistics> =
        coordinatorExec.exec {
            runCatching {
                val classes = assignmentRepository.getTeacherAssignments(
                    TeacherAssignmentFilter(teacherId = teacherId)
                ).getOrNull()?.distinctBy { it.assignmentId.classId }?.size ?: 0
                val todayLessons = lessonRepository.getLessons(
                    LessonFilter(
                        teacherId = teacherId,
                        dateFrom = startOfDay(),
                        dateTo = endOfDay()
                    )
                ).getOrNull()?.size ?: 0
                TeacherStatistics(classesCount = classes, lessonsTodayCount = todayLessons)
            }
        }
}