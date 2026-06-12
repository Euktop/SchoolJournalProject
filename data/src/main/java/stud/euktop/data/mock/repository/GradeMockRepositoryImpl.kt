package stud.euktop.data.mock.repository

import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.repository.GradeRepository
import stud.euktop.domain.utils.loger.logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GradeMockRepositoryImpl @Inject constructor() : GradeRepository {

    private val tag = this::class.java.simpleName

    private val addedGrades = mutableListOf<AddGradeRecord>()
    override suspend fun setGrade(
        lessonId: Int,
        studentId: Int,
        absenceTypes: AbsenceTypes?,
        comment: String?
    ): Result<Unit> {
        val value = absenceTypes?.getGrade()
        val ab = absenceTypes?.getAbsenceTypeId()
        logger?.i(
            tag,
            "addGrade",
            "lessonId=$lessonId, studentId=$studentId, value=$value, absenceTypeId=$ab, comment=$comment"
        )

        // Имитируем успешное сохранение
        addedGrades.add(
            AddGradeRecord(
                lessonId = lessonId,
                studentId = studentId,
                value = value,
                absenceTypeId = ab,
                comment = comment,
                timestamp = System.currentTimeMillis()
            )
        )


        return Result.success(Unit)
    }


    private data class AddGradeRecord(
        val lessonId: Int,
        val studentId: Int,
        val value: Int?,
        val absenceTypeId: Int?,
        val comment: String?,
        val timestamp: Long
    )
}