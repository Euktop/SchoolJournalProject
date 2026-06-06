package stud.euktop.domain.model.student

data class StudentGradesSummary(
    val subjectId: Int,
    val subjectName: String,
    val averageMark: Double?,
    val finalMark: Double?,
    val totalGrades: Int?,
    val goodGrades: Int?,
    val excellentGrades: Int?
)