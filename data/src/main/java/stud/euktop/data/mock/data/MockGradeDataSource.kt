package stud.euktop.data.mock.data

import stud.euktop.domain.model.attendance.AbsenceTypes

internal object MockGradeDataSource {
    private val addedGrades = mutableListOf<GradeRecord>()

    fun addGrade(
        lessonId: Int,
        studentId: Int,
        absenceTypes: AbsenceTypes?,
        comment: String?
    ) {
        addedGrades.add(
            GradeRecord(
                lessonId = lessonId,
                studentId = studentId,
                value = absenceTypes?.getGrade(),
                absenceTypeId = absenceTypes?.getAbsenceTypeId(),
                comment = comment,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    // Метод может быть полезен для написания unit-тестов, чтобы проверить, что данные действительно сохранились
    fun getAddedGrades(): List<GradeRecord> = addedGrades.toList()

    data class GradeRecord(
        val lessonId: Int,
        val studentId: Int,
        val value: Int?,
        val absenceTypeId: Int?,
        val comment: String?,
        val timestamp: Long
    )
}