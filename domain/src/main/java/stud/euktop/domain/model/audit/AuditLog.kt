package stud.euktop.domain.model.audit

import java.time.Instant

data class AuditLog(
    val id: Int,
    val actionType: ActionType,
    val tableName: String,
    val objectId: Int,
    val executorName: String,
    val executorUserId: Int,
    val eventTime: Instant,
    val ipAddress: String,
    val dataChange: DataChange,                  // old / new значения
    val userAgent: String,
    val requestMethod: String,                   // например, POST /api/v1/students/45012
    val origin: String,                          // источник запроса
    val technicalInfo: Map<String, String>? = null // дополнительные поля
) {
    fun toListItem(): AuditLogListItem = AuditLogListItem(
        id = id,
        actionType = actionType,
        tableName = tableName,
        executorName = executorName,
        ipAddress = ipAddress,
        eventTime = eventTime
    )
}