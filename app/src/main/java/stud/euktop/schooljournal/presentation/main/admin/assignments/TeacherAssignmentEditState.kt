// presentation/main/admin/assignments/TeacherAssignmentEditState.kt
package stud.euktop.schooljournal.presentation.main.admin.assignments

import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.schooljournal.presentation.common.base.BaseState
import java.util.Date

data class TeacherAssignmentEditState(
    val assignmentId: AssignmentId? = null,
    val teacher: UserProfile? = null,
    val classInfo: ClassInfo? = null,
    val subject: Subject? = null,
    val validFrom: Date? = null,
    val validTo: Date? = null,
    val isPrimary: Boolean = false,
    val originalTeacherId: Int? = null,
    val originalClassId: Int? = null,
    val originalSubjectId: Int? = null,
    val originalValidFrom: Date? = null,
    val originalValidTo: Date? = null,
    val originalIsPrimary: Boolean = false,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<TeacherAssignmentEditState>() {
    fun isFormValid(): Boolean = teacher != null && classInfo != null && subject != null && validFrom != null
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}