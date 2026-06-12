package stud.euktop.domain.repository

import stud.euktop.domain.model.attendance.AbsenceTypes

interface GradeRepository {
    /**
     * Устанавливает оценку.
     * Если absenceTypes == null, оценка удаляется.
     */
    suspend fun setGrade(
        lessonId: Int,
        studentId: Int,
        absenceTypes: AbsenceTypes?,
        comment: String?
    ): Result<Unit>
}