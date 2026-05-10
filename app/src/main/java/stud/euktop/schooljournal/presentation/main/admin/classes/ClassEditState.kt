package stud.euktop.schooljournal.presentation.main.admin.classes

import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.utils.validation.TextThereValidator
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class ClassEditState(
    override val isLoading: Boolean = false,
    val classId: Int = 0,
    val grade: Int? = null,
    val letter: TextThereValidator = TextThereValidator(),
    val academicYearStart: Int? = null,
    val academicYearEnd: Int? = null,
    val availableSchools: List<School> = emptyList(),
    val availableTeachers: List<UserProfile> = emptyList(),
    val selectedSchool: School? = null,
    val selectedTeacher: UserProfile? = null
) : BaseState<ClassEditState>() {

    fun isEditMode() = classId != 0

    fun isFormValid(): Boolean {
        if (selectedSchool == null) return false
        if (grade == null || grade !in 1..11) return false
        if (!letter.validate()) return false
        if (academicYearStart == null || academicYearEnd == null) return false
        if (academicYearStart > academicYearEnd) return false
        return true
    }

    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}