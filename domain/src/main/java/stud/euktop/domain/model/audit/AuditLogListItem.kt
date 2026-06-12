package stud.euktop.domain.model.audit

import java.time.Instant


data class AuditLogListItem(
    val id: Int,
    val actionType: ActionType,
    val tableName: String,
    val executorName: String,
    val ipAddress: String,
    val eventTime: Instant
)