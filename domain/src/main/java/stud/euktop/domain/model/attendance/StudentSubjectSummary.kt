package stud.euktop.domain.model.attendance

data class StudentSubjectSummary(
    val subjectId: Int,
    val subjectName: String,
    val averageMark: Double?,
    val finalMark: Int?,
    val teacherName: String? = null,
    val attendancePercent: Int? = null
)