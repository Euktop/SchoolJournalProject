// data/src/main/java/stud/euktop/data/mock/repository/StudentMockRepositoryImpl.kt
package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockClassDataSource
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockStudentDataSource
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentMockRepositoryImpl @Inject constructor() : StudentRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getSubjectsSummary(studentId: Int?): Result<List<StudentSubjectSummary>> {
        logger?.i(tag, "getSubjectsSummary started", "studentId=$studentId")
        MockDelayService.delay()
        return try {
            val summary = MockStudentDataSource.getSubjectsSummary(studentId ?: 0)
            Result.success(summary)
        } catch (e: Exception) {
            logger?.e(tag, "getSubjectsSummary failed", e, "studentId=$studentId")
            Result.failure(e)
        }
    }

    override suspend fun getSubjectMarks(subjectId: Int, studentId: Int?): Result<List<StudentSubjectMark>> {
        logger?.i(tag, "getSubjectMarks started", "subjectId=$subjectId, studentId=$studentId")
        MockDelayService.delay()
        return try {
            val marks = MockStudentDataSource.getSubjectMarks(studentId ?: 0, subjectId)
            Result.success(marks)
        } catch (e: Exception) {
            logger?.e(tag, "getSubjectMarks failed", e, "subjectId=$subjectId, studentId=$studentId")
            Result.failure(e)
        }
    }

    override suspend fun getStudentClass(studentId: Int?): Result<ClassInfo> {
        logger?.i(tag, "getStudentClass started", "studentId=$studentId")
        MockDelayService.delay()
        return try {
            val classInfo = MockClassDataSource.getClassByStudent(studentId ?: 0)
            if (classInfo != null) {
                Result.success(classInfo)
            } else {
                val ex = NoSuchElementException("Student class not found")
                logger?.e(tag, "getStudentClass failed", ex, "studentId=$studentId not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "getStudentClass exception", e, "studentId=$studentId")
            Result.failure(e)
        }
    }
}