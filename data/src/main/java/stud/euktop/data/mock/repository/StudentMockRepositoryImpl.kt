package stud.euktop.data.mock.repository

import stud.euktop.data.local.storage.contract.UserIdStorage
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
import stud.euktop.domain.model.student.SubjectTrend
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentMockRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler,
    private val userIdStorage: UserIdStorage
) : StudentRepository {
    private val tag = this.toSimpleTag()

    private suspend fun getCurrentUserId(): Int {
        return userIdStorage.getUserId() ?: 0
    }

    override suspend fun getSubjectsSummary(studentId: Int?): Result<List<StudentSubjectSummary>> = apiErrorHandler.safeApiCall {
        val currentId = studentId ?: getCurrentUserId()
        logger?.i(tag, "getSubjectsSummary", "studentId=$currentId")
        MockDelayService.delay()
        MockStudentDataSource.getSubjectsSummary(currentId).ifEmpty { emptyList() }
    }

    override suspend fun getSubjectMarks(
        subjectId: Int,
        studentId: Int?,
        startDate: Date?,
        endDate: Date?,
        offset: Int?,
        limit: Int?
    ): Result<List<StudentSubjectMark>> = apiErrorHandler.safeApiCall {
        val currentId = studentId ?: getCurrentUserId()
        logger?.i(tag, "getSubjectMarks", "subjectId=$subjectId, studentId=$currentId")
        MockDelayService.delay()
        var marks = MockStudentDataSource.getSubjectMarks(currentId, subjectId)
        if (startDate != null) marks = marks.filter { it.date >= startDate }
        if (endDate != null) marks = marks.filter { it.date <= endDate }
        val offsetVal = offset ?: 0
        val limitVal = limit ?: Int.MAX_VALUE
        marks.drop(offsetVal).take(limitVal).ifEmpty { emptyList() }
    }

    override suspend fun getStudentClass(studentId: Int?): Result<ClassInfo> = apiErrorHandler.safeApiCall {
        val currentId = studentId ?: getCurrentUserId()
        logger?.i(tag, "getStudentClass", "studentId=$currentId")
        MockDelayService.delay()
        MockClassDataSource.getClassByStudent(currentId)
            ?: ClassInfo.createObject(
                classId = 0, schoolId = 0, grade = 0, letter = "А",
                academicYearStart = 2024, academicYearEnd = 2025, teacherId = null
            )
    }

    override suspend fun getGradesSummary(
        studentId: Int?,
        periodStart: Date?,
        periodEnd: Date?
    ): Result<List<StudentGradesSummary>> = apiErrorHandler.safeApiCall {
        val currentId = studentId ?: getCurrentUserId()
        logger?.i(tag, "getGradesSummary", "studentId=$currentId")
        MockDelayService.delay()
        MockStudentDataSource.getGradesSummary(currentId).ifEmpty {
            listOf(
                StudentGradesSummary(
                    subjectId = 0, subjectName = "Неизвестный предмет", averageMark = 0.0, finalMark = 0.0,
                    totalGrades = 0, goodGrades = 0, excellentGrades = 0
                )
            )
        }
    }

    override suspend fun getMarksAggregated(
        subjectId: Int,
        studentId: Int?,
        startDate: Date?,
        endDate: Date?,
        maxPoints: Int?
    ): Result<List<StudentMarksAggregated>> = apiErrorHandler.safeApiCall {
        val currentId = studentId ?: getCurrentUserId()
        logger?.i(tag, "getMarksAggregated", "subjectId=$subjectId, studentId=$currentId")
        MockDelayService.delay()
        MockStudentDataSource.getMarksAggregated(currentId, subjectId).ifEmpty {
            listOf(StudentMarksAggregated(date = Date(), averageMark = 0.0, marksCount = 0))
        }
    }

    override suspend fun getOverallAverage(
        studentId: Int?,
        startDate: Date?,
        endDate: Date?
    ): Result<StudentOverallAverage> = apiErrorHandler.safeApiCall {
        val currentId = studentId ?: getCurrentUserId()
        logger?.i(tag, "getOverallAverage", "studentId=$currentId")
        MockDelayService.delay()
        MockStudentDataSource.getOverallAverage(currentId)
            ?: StudentOverallAverage(averageMark = 0.0, totalGrades = 0, goodGrades = 0, excellentGrades = 0)
    }

    override suspend fun getStudentSchedule(
        studentId: Int?,
        startDate: Date?,
        endDate: Date?
    ): Result<List<StudentScheduleItem>> = apiErrorHandler.safeApiCall {
        val currentId = studentId ?: getCurrentUserId()
        logger?.i(tag, "getStudentSchedule", "studentId=$currentId")
        MockDelayService.delay()
        MockStudentDataSource.getStudentSchedule(currentId).ifEmpty {
            listOf(
                StudentScheduleItem(
                    lessonId = 0, date = Date(), startTime = "00:00", endTime = "00:00",
                    topic = "Нет расписания", subjectName = "Нет предмета",
                    teacherLastName = "Нет", teacherFirstName = "Нет", teacherSurName = "Нет",
                    roomName = "Нет", locationAddress = null
                )
            )
        }
    }

    override suspend fun getSubjectTrend(
        subjectId: Int,
        studentId: Int?,
        weeks: Int
    ): Result<SubjectTrend> = apiErrorHandler.safeApiCall {
        val currentId = studentId ?: getCurrentUserId()
        logger?.i(tag, "getSubjectTrend", "subjectId=$subjectId, studentId=$currentId, weeks=$weeks")
        MockDelayService.delay()
        SubjectTrend(value = 0.0, isPositive = true, formattedString = "Без изменений")
    }
}