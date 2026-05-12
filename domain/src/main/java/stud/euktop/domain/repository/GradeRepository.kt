package stud.euktop.domain.repository

import stud.euktop.domain.model.attendance.AbsenceTypes

interface GradeRepository {
    suspend fun addGrade(
        lessonId: Int,
        studentId: Int,
        absenceTypes: AbsenceTypes,
        comment: String?
    ): Result<Unit>
}