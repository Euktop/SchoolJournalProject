// data/src/main/java/stud/euktop/data/repository/StudentRepositoryImpl.kt
package stud.euktop.data.repository

import com.schooljournal.api.StudentApi
import stud.euktop.data.map.toDomain
import stud.euktop.data.map.toStudentClassInfo
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.repository.StudentRepository
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
        studentId: Int?
    ): Result<List<StudentSubjectMark>> =
        errorHandler.safeApiCall {
            val dtos = studentApi.apiStudentMarksSubjectIdGet(
                subjectId = subjectId,
                studentId = studentId,
                startDate = null,
                endDate = null,
                offset = null,
                limit = null
            )
            dtos.map { it.toDomain() }
        }

    override suspend fun getStudentClass(studentId: Int?): Result<ClassInfo> =
        errorHandler.safeApiCall {
            val dto = studentApi.apiStudentClassGet(studentId = studentId)
            dto.toStudentClassInfo()
        }
}