package stud.euktop.domain.model

data class TeacherAssignment(
    val teacherId: Int,
    val teacherName: String,
    val classId: Int,
    val className: String,
    val subjectId: Int,
    val subjectName: String,
    val validFrom: String,   // "dd.MM.yyyy"
    val validTo: String? = null
)