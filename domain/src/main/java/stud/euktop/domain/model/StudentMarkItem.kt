package stud.euktop.domain.model

data class StudentMarkItem(
    val studentId: Int,
    val lastName: String,
    val firstName: String,
    val surName: String?,
    val absenceCode: AbsenceTypes?,
    val comment: String?
)