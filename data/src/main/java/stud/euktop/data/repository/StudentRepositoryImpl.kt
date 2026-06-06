// data/src/main/java/stud/euktop/data/repository/StudentRepositoryImpl.kt
package stud.euktop.data.repository

import com.schooljournal.api.StudentApi
import stud.euktop.data.map.toDate
import stud.euktop.data.map.toDomain
import stud.euktop.data.map.toLocalDateTime
import stud.euktop.data.map.toStudentClassInfo
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.model.schedule.StudentScheduleItem
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.student.StudentGradesSummary
import stud.euktop.domain.model.student.StudentMarksAggregated
import stud.euktop.domain.model.student.StudentOverallAverage
import stud.euktop.domain.repository.StudentRepository
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepositoryImpl @Inject constructor(
    private val studentApi: StudentApi,
    private val errorHandler: ApiErrorHandler
) : StudentRepository {

    override suspend fun getSubjectsSummary(studentId: Int?): Result<List<StudentSubjectSummary>> =
        errorHandler.safeApiCall {
            val dtos = studentApi.apiStudentSubjectsGet(studentId = studentId)
            dtos.map { it.toDomain() }
        }

    override suspend fun getSubjectMarks(
        subjectId: Int,
        studentId: Int?,
        startDate: Date?,
        endDate: Date?,
        offset: Int?,
        limit: Int?
    ): Result<List<StudentSubjectMark>> =
        errorHandler.safeApiCall {
            val dtos = studentApi.apiStudentMarksSubjectIdGet(
                subjectId = subjectId,
                studentId = studentId,
                startDate = startDate?.toLocalDateTime(),
                endDate = endDate?.toLocalDateTime(),
                offset = offset,
                limit = limit
            )
            dtos.map { it.toDomain() }
        }

    override suspend fun getStudentClass(studentId: Int?): Result<ClassInfo> =
        errorHandler.safeApiCall {
            val dto = studentApi.apiStudentClassGet(studentId = studentId)
            dto.toStudentClassInfo()
        }

    override suspend fun getGradesSummary(
        studentId: Int?,
        periodStart: Date?,
        periodEnd: Date?
    ): Result<List<StudentGradesSummary>> =
        errorHandler.safeApiCall {
            val dtos = studentApi.apiStudentGradesSummaryGet(
                studentId = studentId,
                periodStart = periodStart?.toLocalDateTime(),
                periodEnd = periodEnd?.toLocalDateTime()
            )
            dtos.map { it.toDomain() }
        }

    override suspend fun getMarksAggregated(
        subjectId: Int,
        studentId: Int?,
        startDate: Date?,
        endDate: Date?,
        maxPoints: Int?
    ): Result<List<StudentMarksAggregated>> =
        errorHandler.safeApiCall {
            val dtos = studentApi.apiStudentMarksAggregatedGet(
                subjectId = subjectId,
                studentId = studentId,
                startDate = startDate?.toLocalDateTime(),
                endDate = endDate?.toLocalDateTime(),
                maxPoints = maxPoints
            )
            dtos.map { it.toDomain() }
        }

    override suspend fun getOverallAverage(
        studentId: Int?,
        startDate: Date?,
        endDate: Date?
    ): Result<StudentOverallAverage> =
        errorHandler.safeApiCall {
            val dto = studentApi.apiStudentOverallAverageGet(
                studentId = studentId,
                startDate = startDate?.toLocalDateTime(),
                endDate = endDate?.toLocalDateTime()
            )
            dto.toDomain()
        }

    override suspend fun getStudentSchedule(
        studentId: Int?,
        startDate: Date?,
        endDate: Date?
    ): Result<List<StudentScheduleItem>> = errorHandler.safeApiCall {
        val dtos = studentApi.apiStudentScheduleGet(
            studentId = studentId,
            startDate = startDate?.toLocalDateTime(),
            endDate = endDate?.toLocalDateTime()
        )
        dtos.map { dto ->
            StudentScheduleItem(
                lessonId = dto.lessonId ?: 0,
                date = dto.date?.toDate() ?: Date(),
                startTime = dto.startTime ?: "",
                endTime = dto.endTime ?: "",
                topic = dto.topic ?: "",
                subjectName = dto.subjectName ?: "",
                teacherLastName = dto.teacherLastName,
                teacherFirstName = dto.teacherFirstName,
                teacherSurName = dto.teacherSurName,
                roomName = dto.roomName,
                locationAddress = dto.locationAddress
            )
        }
    }
}