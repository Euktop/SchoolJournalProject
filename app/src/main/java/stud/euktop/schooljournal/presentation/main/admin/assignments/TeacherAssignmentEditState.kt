package stud.euktop.schooljournal.presentation.main.admin.assignments

import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.schooljournal.presentation.main.admin.common.base.BaseEditState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TeacherAssignmentEditState(
    override val isLoading: Boolean = false,
    val assignmentId: Int = 0,
    val teacher: UserInfo? = null,
    val classInfo: ClassInfo? = null,
    val subject: Subject? = null,
    val validFrom: Date? = null,
    val validTo: Date? = null,
    val isPrimary: Boolean = false,
    val availableTeachers: List<UserInfo> = emptyList(),
    val availableClasses: List<ClassInfo> = emptyList(),
    val availableSubjects: List<Subject> = emptyList()
) : BaseEditState<TeacherAssignmentEditState>() {

    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun isEditMode() = assignmentId != 0

    override fun isFormValid(): Boolean {
        if (teacher == null) return false
        if (classInfo == null) return false
        if (subject == null) return false
        if (validFrom == null) return false
        if (validTo != null && validTo < validFrom) return false
        return true
    }

    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}