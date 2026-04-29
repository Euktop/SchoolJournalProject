package stud.euktop.domain.model

data class ClassInfo(
    val classId: Int,
    val schoolName: String?,
    val grade: Int,
    val letter: String,
    val academicYearStart: Int,
    val academicYearEnd: Int,
    val teacherId: Int? = null,
    val teacherName: String? = null
)
