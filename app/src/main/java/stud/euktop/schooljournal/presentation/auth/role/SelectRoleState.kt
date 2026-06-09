package stud.euktop.schooljournal.presentation.auth.role

import stud.euktop.domain.model.user.Role
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class SelectRoleState(
    val roles: List<RoleItem> = emptyList(),
    val selectedRole: Role? = null,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<SelectRoleState>() {
    
    fun isButtonActive(): Boolean = selectedRole != null
    
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): SelectRoleState =
        copy(loadingMap = loadingMap)
}