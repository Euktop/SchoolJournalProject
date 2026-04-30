package stud.euktop.domain.model.attendance

/**
 * Информация об оценке/пропуске ученика для отображения в списке (учитель).
 * Использует ID ученика (не полный объект) для уменьшения данных.
 */
data class StudentMarkItem(
    val studentId: Int,
    val lastName: String,
    val firstName: String,
    val surName: String?,
    val absenceCode: AbsenceTypes?,
    val comment: String?
)