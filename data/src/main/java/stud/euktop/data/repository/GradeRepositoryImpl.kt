package stud.euktop.data.repository

import com.schooljournal.api.GradesApi
import com.schooljournal.model.AddGradeRequest
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.repository.GradeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GradeRepositoryImpl @Inject constructor(
    private val gradesApi: GradesApi,
    private val errorHandler: ApiErrorHandler
) : GradeRepository {

    override suspend fun setGrade(
        lessonId: Int,
        studentId: Int,
        absenceTypes: AbsenceTypes?,
        comment: String?
    ): Result<Unit> = errorHandler.safeApiCall {
        val request = AddGradeRequest(
            lessonId = lessonId,
            studentId = studentId,
            value = absenceTypes?.getGrade(),
            absenceTypeId = absenceTypes?.getAbsenceTypeId(),
            comment = comment
        )
        gradesApi.apiGradesPost(request)
        Unit
    }
}