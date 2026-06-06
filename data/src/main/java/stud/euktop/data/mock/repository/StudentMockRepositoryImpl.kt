package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockClassDataSource
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockStudentDataSource
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.model.schedule.StudentScheduleItem
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.student.StudentGradesSummary
import stud.euktop.domain.model.student.StudentMarksAggregated
import stud.euktop.domain.model.student.StudentOverallAverage
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentMockRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler
) : StudentRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getSubjectsSummary(studentId: Int?): Result<List<StudentSubjectSummary>> {
        logger?.i(tag, "getSubjectsSummary started", "studentId=$studentId")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            MockStudentDataSource.getSubjectsSummary(studentId ?: 0)
        }
    }

    override suspend fun getSubjectMarks(
        subjectId: Int,
        studentId: Int?,
        startDate: Date?,
        endDate: Date?,
        offset: Int?,
        limit: Int?
    ): Result<List<StudentSubjectMark>> {
        logger?.i(
            tag, "getSubjectMarks started",
            "subjectId=$subjectId, studentId=$studentId, startDate=$startDate, endDate=$endDate, offset=$offset, limit=$limit"
        )
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val marks = MockStudentDataSource.getSubjectMarks(studentId ?: 0, subjectId)
            var filtered = marks
            if (startDate != null) filtered = filtered.filter { it.date >= startDate }
            if (endDate != null) filtered = filtered.filter { it.date <= endDate }
            val offsetVal = offset ?: 0
            val limitVal = limit ?: Int.MAX_VALUE
            filtered.drop(offsetVal).take(limitVal)
        }
    }

    override suspend fun getStudentClass(studentId: Int?): Result<ClassInfo> {
        logger?.i(tag, "getStudentClass started", "studentId=$studentId")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            MockClassDataSource.getClassByStudent(studentId ?: 0)
                ?: throw NoSuchElementException("Student class not found")
        }
    }

    override suspend fun getGradesSummary(
        studentId: Int?,
        periodStart: Date?,
        periodEnd: Date?
    ): Result<List<StudentGradesSummary>> {
        logger?.i(
            tag, "getGradesSummary started",
            "studentId=$studentId, periodStart=$periodStart, periodEnd=$periodEnd"
        )
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            // TODO: реализовать в MockStudentDataSource, пока заглушка
            emptyList()
        }
    }

    override suspend fun getMarksAggregated(
        subjectId: Int,
        studentId: Int?,
        startDate: Date?,
        endDate: Date?,
        maxPoints: Int?
    ): Result<List<StudentMarksAggregated>> {
        logger?.i(
            tag, "getMarksAggregated started",
            "subjectId=$subjectId, studentId=$studentId, startDate=$startDate, endDate=$endDate, maxPoints=$maxPoints"
        )
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            // TODO: реализовать в MockStudentDataSource, пока заглушка
            emptyList()
        }
    }

    override suspend fun getOverallAverage(
        studentId: Int?,
        startDate: Date?,
        endDate: Date?
    ): Result<StudentOverallAverage> {
        logger?.i(
            tag, "getOverallAverage started",
            "studentId=$studentId, startDate=$startDate, endDate=$endDate"
        )
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            StudentOverallAverage(
                averageMark = null,
                totalGrades = null,
                goodGrades = null,
                excellentGrades = null
            )
        }
    }

    override suspend fun getStudentSchedule(
        studentId: Int?,
        startDate: Date?,
        endDate: Date?
    ): Result<List<StudentScheduleItem>> = apiErrorHandler.safeApiCall {
        MockDelayService.delay()
        // Генерируем тестовые данные для демонстрации
        val now = Date()
        listOf(
            StudentScheduleItem(
                lessonId = 1,
                date = now,
                startTime = "09:00",
                endTime = "09:45",
                topic = "Квадратные уравнения",
                subjectName = "Математика",
                teacherLastName = "Петрова",
                teacherFirstName = "Анна",
                teacherSurName = "Сергеевна",
                roomName = "101",
                locationAddress = null
            ),
            StudentScheduleItem(
                lessonId = 2,
                date = now,
                startTime = "10:00",
                endTime = "10:45",
                topic = "Дискриминант",
                subjectName = "Математика",
                teacherLastName = "Петрова",
                teacherFirstName = "Анна",
                teacherSurName = "Сергеевна",
                roomName = "101",
                locationAddress = null
            ),
            StudentScheduleItem(
                lessonId = 3,
                date = now,
                startTime = "11:00",
                endTime = "11:45",
                topic = "Русский язык",
                subjectName = "Русский язык",
                teacherLastName = "Иванов",
                teacherFirstName = "Иван",
                teacherSurName = "Иванович",
                roomName = "203",
                locationAddress = null
            )
        )
    }
}