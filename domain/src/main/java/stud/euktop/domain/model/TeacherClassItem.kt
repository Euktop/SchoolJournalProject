package stud.euktop.domain.model

data class TeacherClassItem(
    val classId: Int,
    val schoolName: String?,
    val grade: Int,
    val letter: String,
    val academicYearStart: Int,
    val academicYearEnd: Int,
    val subjectId: Int,
    val subjectName: String?
)