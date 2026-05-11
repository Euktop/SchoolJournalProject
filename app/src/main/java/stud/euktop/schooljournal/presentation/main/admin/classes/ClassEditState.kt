package stud.euktop.schooljournal.presentation.main.admin.classes

import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.utils.validation.TextThereValidator
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class ClassEditState(
    val grade: Int? = null,
    val letter: TextThereValidator = TextThereValidator(),
    val academicYearStart: Int? = null,
    val academicYearEnd: Int? = null,
    val school: School? = null,
    val classTeacher: UserProfile? = null,
    val originalGrade: Int? = null,
    val originalLetter: String = "",
    val originalYearStart: Int? = null,
    val originalYearEnd: Int? = null,
    val originalSchoolId: Int? = null,
    val originalClassTeacherId: Int? = null,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<ClassEditState>() {
    fun isFormValid(): Boolean = when {
        grade == null || grade !in 1..11 -> false
        !letter.validate() -> false
        academicYearStart == null || academicYearEnd == null -> false
        academicYearStart > academicYearEnd -> false
        else -> true
    }

    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}