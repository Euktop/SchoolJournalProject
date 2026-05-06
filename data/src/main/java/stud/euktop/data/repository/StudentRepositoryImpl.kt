// StudentRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockStudentDataSource
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepositoryImpl @Inject constructor() : StudentRepository {
    private val tag = this.toSimpleTag()

    private val studentClassMap = mapOf(
        1 to 1,   // Иванов (админ, но для теста – класс 1)
        3 to 1,   // Сидоров
        4 to 1,   // Борисова
        5 to 1    // Дмитриева
    )

    override suspend fun getSubjectsSummary(studentId: Int): Result<List<StudentSubjectSummary>> {
        logger?.i(tag, "getSubjectsSummary started", "studentId=$studentId")
        MockDelayService.delay()
        return try {
            val summary = MockStudentDataSource.getSubjectsSummary(studentId)
            logger?.i(
                tag,
                "getSubjectsSummary succeeded",
                "studentId=$studentId, count=${summary.size}"
            )
            Result.success(summary)
        } catch (e: Exception) {
            logger?.e(tag, "getSubjectsSummary failed", e, "studentId=$studentId")
            Result.failure(e)
        }
    }

    override suspend fun getSubjectMarks(
        studentId: Int,
        subjectId: Int
    ): Result<List<StudentSubjectMark>> {
        logger?.i(tag, "getSubjectMarks started", "studentId=$studentId, subjectId=$subjectId")
        MockDelayService.delay()
        return try {
            val marks = MockStudentDataSource.getSubjectMarks(studentId, subjectId)
            logger?.i(
                tag,
                "getSubjectMarks succeeded",
                "studentId=$studentId, subjectId=$subjectId, count=${marks.size}"
            )
            Result.success(marks)
        } catch (e: Exception) {
            logger?.e(
                tag,
                "getSubjectMarks failed",
                e,
                "studentId=$studentId, subjectId=$subjectId"
            )
            Result.failure(e)
        }
    }

    override suspend fun getStudentClassId(studentId: Int): Result<Int> {
        logger?.i(tag, "getStudentClassId started", "studentId=$studentId")
        MockDelayService.delay()
        return try {
            val classId = studentClassMap[studentId]
            if (classId != null) {
                logger?.i(
                    tag,
                    "getStudentClassId succeeded",
                    "studentId=$studentId, classId=$classId"
                )
                Result.success(classId)
            } else {
                val ex = NoSuchElementException("Student class not found")
                logger?.e(tag, "getStudentClassId failed", ex, "studentId=$studentId not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "getStudentClassId exception", e, "studentId=$studentId")
            Result.failure(e)
        }
    }
}