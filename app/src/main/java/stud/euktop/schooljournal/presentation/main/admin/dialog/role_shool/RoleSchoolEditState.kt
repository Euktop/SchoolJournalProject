package stud.euktop.schooljournal.presentation.main.admin.dialog.role_shool

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class RoleSchoolEditState(override val isLoading: Boolean = false) :
    BaseState<RoleSchoolEditState>() {
    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}