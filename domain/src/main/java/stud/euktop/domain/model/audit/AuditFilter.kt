package stud.euktop.domain.model.audit

import java.util.Date

data class AuditFilter(
    val userQuery: String? = null,          // имя или ID
    val actionTypes: Set<ActionType> = emptySet(),
    val tableName: String? = null,
    val fromDate: Date? = null,
    val toDate: Date? = null
)