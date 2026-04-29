package stud.euktop.domain.model

data class StudentSubjectMark(
    val date: String,           // "28.04.2026"
    val value: Int?,            // 2..5 (null — если пропуск)
    val absenceCode: String?,   // "н", "б" и т.п.
    val comment: String?        // комментарий учителя
)