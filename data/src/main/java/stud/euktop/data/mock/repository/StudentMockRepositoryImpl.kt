package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockClassDataSource
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockStudentDataSource
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
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

    override suspend fun getSubjectMarks(subjectId: Int, studentId: Int?): Result<List<StudentSubjectMark>> {
        logger?.i(tag, "getSubjectMarks started", "subjectId=$subjectId, studentId=$studentId")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            MockStudentDataSource.getSubjectMarks(studentId ?: 0, subjectId)
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
}