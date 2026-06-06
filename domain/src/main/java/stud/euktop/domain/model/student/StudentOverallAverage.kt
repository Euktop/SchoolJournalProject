package stud.euktop.domain.model.student

data class StudentOverallAverage(
    val averageMark: Double?,
    val totalGrades: Int?,
    val goodGrades: Int?,
    val excellentGrades: Int?
)