package stud.euktop.data.repository

import com.schooljournal.api.GradesApi
import com.schooljournal.api.LessonsApi
import com.schooljournal.api.StudentApi
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.repository.GradeRepository
import javax.inject.Inject

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
        //TODO("Нужно обновить api")
    }
}