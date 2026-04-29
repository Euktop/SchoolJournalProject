package stud.euktop.domain.model

data class StudentSubjectSummary(
    val subjectId: Int,
    val subjectName: String,
    val averageMark: Double?,   // средний балл
    val finalMark: Int?,        // итоговая оценка (4 или 5)
    val teacherName: String?
)