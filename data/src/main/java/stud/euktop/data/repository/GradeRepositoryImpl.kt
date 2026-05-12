package stud.euktop.data.repository

import com.schooljournal.api.GradesApi
import com.schooljournal.model.AddGradeRequest
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.repository.GradeRepository
import javax.inject.Inject

class GradeRepositoryImpl @Inject constructor(
    private val gradesApi: GradesApi,
    private val errorHandler: ApiErrorHandler
) : GradeRepository {

    override suspend fun addGrade(
        lessonId: Int,
        studentId: Int,
        absenceTypes: AbsenceTypes,
        comment: String?
    ): Result<Unit> = errorHandler.safeApiCall {
        val grade = absenceTypes.getGrade()
        val ab = absenceTypes.getAbsenceTypeId()
        gradesApi.apiGradesPost(
            AddGradeRequest(
                lessonId = lessonId,
                studentId = studentId,
                value = grade,
                absenceTypeId = ab,
                comment = comment
            )
        )
    }
}