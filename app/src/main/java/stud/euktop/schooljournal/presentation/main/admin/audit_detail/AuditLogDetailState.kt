package stud.euktop.schooljournal.presentation.main.admin.audit_detail

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class AuditLogDetailState(
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<AuditLogDetailState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}