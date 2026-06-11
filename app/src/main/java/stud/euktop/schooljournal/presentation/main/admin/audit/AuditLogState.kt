package stud.euktop.schooljournal.presentation.main.admin.audit

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class AuditLogState(override val loadingMap: Map<String, Boolean> = emptyMap()) :
    BaseState<AuditLogState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}