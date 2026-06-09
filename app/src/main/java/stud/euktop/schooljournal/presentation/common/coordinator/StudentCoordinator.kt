package stud.euktop.schooljournal.presentation.common.coordinator

import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.student.StudentGradesSummary
import stud.euktop.domain.model.student.StudentMarksAggregated
import stud.euktop.domain.model.student.StudentOverallAverage
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentCoordinator @Inject constructor(
    private val studentRepository: StudentRepository,
    private val coordinatorExec: CoordinatorExec
) {

    suspend fun getSubjectsSummary(studentId: Int? = null): CoordinatorResult<List<StudentSubjectSummary>> =
        coordinatorExec.exec { studentRepository.getSubjectsSummary(studentId) }

    suspend fun getSubjectMarks(
        subjectId: Int,
        studentId: Int? = null,
        startDate: Date? = null,
        endDate: Date? = null,
        offset: Int? = null,
        limit: Int? = null
    ): CoordinatorResult<List<StudentSubjectMark>> =
        coordinatorExec.exec {
            studentRepository.getSubjectMarks(
                subjectId,
                studentId,
                startDate,
                endDate,
                offset,
                limit
            )
        }

    suspend fun getStudentClass(studentId: Int? = null): CoordinatorResult<ClassInfo> =
        coordinatorExec.exec { studentRepository.getStudentClass(studentId) }

    // Новые методы
    suspend fun getGradesSummary(
        studentId: Int? = null,
        periodStart: Date? = null,
        periodEnd: Date? = null
    ): CoordinatorResult<List<StudentGradesSummary>> =
        coordinatorExec.exec {
            studentRepository.getGradesSummary(
                studentId,
                periodStart,
                periodEnd
            )
        }

    suspend fun getMarksAggregated(
        subjectId: Int,
        studentId: Int? = null,
        startDate: Date? = null,
        endDate: Date? = null,
        maxPoints: Int? = null
    ): CoordinatorResult<List<StudentMarksAggregated>> =
        coordinatorExec.exec {
            studentRepository.getMarksAggregated(
                subjectId,
                studentId,
                startDate,
                endDate,
                maxPoints
            )
        }

    suspend fun getOverallAverage(
        studentId: Int? = null,
        startDate: Date? = null,
        endDate: Date? = null
    ): CoordinatorResult<StudentOverallAverage> =
        coordinatorExec.exec { studentRepository.getOverallAverage(studentId, startDate, endDate) }
}