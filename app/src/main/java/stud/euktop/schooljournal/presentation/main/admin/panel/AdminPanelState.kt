package stud.euktop.schooljournal.presentation.main.admin.panel

import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.assignment.TeacherAssignment
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class AdminPanelState(
    override val isLoading: Boolean = false,
    val users: List<UserInfo> = emptyList(),
    val classes: List<ClassInfo> = emptyList(),
    val subjects: List<Subject> = emptyList(),
    val assignments: List<TeacherAssignment> = emptyList()
) : BaseState<AdminPanelState>() {
    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}