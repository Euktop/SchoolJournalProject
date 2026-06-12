package stud.euktop.domain.model.audit

data class AuditStatistics(
    val totalEvents: Long,
    val criticalEventsCount: Long,
    val todayEvents: Long,
    val activeSessions: Int
)