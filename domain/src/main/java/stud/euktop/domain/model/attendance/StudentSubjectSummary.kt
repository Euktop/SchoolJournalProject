package stud.euktop.domain.model.attendance

/**
 * Сводка по предмету для ученика/родителя (средний балл, итог).
 * @param subjectId идентификатор предмета
 * @param subjectName название предмета
 * @param averageMark средний балл
 * @param finalMark итоговая оценка (может быть null)
 * @param teacherName ФИО учителя (информационный, избыточно, но допустимо для view)
 */
data class StudentSubjectSummary(
    val subjectId: Int,
    val subjectName: String,
    val averageMark: Double?,
    val finalMark: Int?,
    val teacherName: String?
)