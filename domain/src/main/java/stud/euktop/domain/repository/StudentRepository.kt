package stud.euktop.domain.repository

import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.model.schedule.StudentScheduleItem
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.student.StudentGradesSummary
import stud.euktop.domain.model.student.StudentMarksAggregated
import stud.euktop.domain.model.student.StudentOverallAverage
import java.util.Date

interface StudentRepository {
    suspend fun getSubjectsSummary(studentId: Int? = null): Result<List<StudentSubjectSummary>>
    suspend fun getSubjectMarks(
        subjectId: Int,
        studentId: Int? = null,
        startDate: Date? = null,
        endDate: Date? = null,
        offset: Int? = null,
        limit: Int? = null
    ): Result<List<StudentSubjectMark>>

    suspend fun getStudentClass(studentId: Int? = null): Result<ClassInfo>

    // Новые методы
    suspend fun getGradesSummary(
        studentId: Int? = null, periodStart: Date? = null, periodEnd: Date? = null
    ): Result<List<StudentGradesSummary>>

    suspend fun getMarksAggregated(
        subjectId: Int,
        studentId: Int? = null,
        startDate: Date? = null,
        endDate: Date? = null,
        maxPoints: Int? = null
    ): Result<List<StudentMarksAggregated>>

    suspend fun getOverallAverage(
        studentId: Int? = null, startDate: Date? = null, endDate: Date? = null
    ): Result<StudentOverallAverage>
    suspend fun getStudentSchedule(
        studentId: Int? = null,
        startDate: Date? = null,
        endDate: Date? = null
    ): Result<List<StudentScheduleItem>>
}