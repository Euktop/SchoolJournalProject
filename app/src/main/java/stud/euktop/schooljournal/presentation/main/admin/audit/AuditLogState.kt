package stud.euktop.schooljournal.presentation.main.admin.audit

import stud.euktop.domain.model.audit.AuditFilter
import stud.euktop.domain.model.audit.AuditLogListItem
import stud.euktop.domain.model.audit.AuditStatistics
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class AuditLogState(
    val logs: List<AuditLogListItem> = emptyList(),
    val filter: AuditFilter = AuditFilter(),
    val statistics: AuditStatistics? = null,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<AuditLogState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}