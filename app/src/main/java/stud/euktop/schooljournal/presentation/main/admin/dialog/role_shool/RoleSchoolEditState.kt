package stud.euktop.schooljournal.presentation.main.admin.dialog.role_shool

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class RoleSchoolEditState(
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<RoleSchoolEditState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): RoleSchoolEditState =
        copy(loadingMap = loadingMap)
}