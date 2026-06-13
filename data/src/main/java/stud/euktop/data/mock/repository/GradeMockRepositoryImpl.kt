package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.data.MockGradeDataSource
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.repository.GradeRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GradeMockRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler
) : GradeRepository {
    private val tag = this.toSimpleTag()

    override suspend fun setGrade(
        lessonId: Int,
        studentId: Int,
        absenceTypes: AbsenceTypes?,
        comment: String?
    ): Result<Unit> = apiErrorHandler.safeApiCall {
        logger?.i(
            tag,
            "setGrade",
            "lessonId=$lessonId, studentId=$studentId, absenceTypes=$absenceTypes, comment=$comment"
        )
        MockDelayService.delay()
        MockGradeDataSource.addGrade(lessonId, studentId, absenceTypes, comment)
        Unit
    }
}