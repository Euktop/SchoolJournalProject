package stud.euktop.data.mock.data

import stud.euktop.domain.model.audit.ActionType
import stud.euktop.domain.model.audit.AuditFilter
import stud.euktop.domain.model.audit.AuditLog
import stud.euktop.domain.model.audit.AuditLogListItem
import stud.euktop.domain.model.audit.AuditStatistics
import stud.euktop.domain.model.audit.DataChange
import java.time.Instant

internal object MockAuditDataSource {
    private val mockLogs = listOf(
        AuditLogListItem(1, ActionType.CREATE, "users", "Admin_Ivanov", "192.168.1.10", Instant.now().minusSeconds(3600)),
        AuditLogListItem(2, ActionType.UPDATE, "classes", "Director_Petrova", "192.168.1.15", Instant.now().minusSeconds(7200)),
        AuditLogListItem(3, ActionType.LOGIN, "auth", "Teacher_Sidorov", "10.0.0.5", Instant.now().minusSeconds(10000)),
        AuditLogListItem(4, ActionType.DELETE, "subjects", "Admin_Ivanov", "192.168.1.10", Instant.now().minusSeconds(15000)),
        AuditLogListItem(5, ActionType.UPDATE, "grades", "Teacher_Sidorov", "10.0.0.5", Instant.now().minusSeconds(20000))
    )

    fun getLogs(filter: AuditFilter, limit: Int, offset: Int): List<AuditLogListItem> {
        val filtered = mockLogs.filter { log ->
            (filter.userQuery?.let { log.executorName.contains(it, ignoreCase = true) } == true) &&
            (filter.actionTypes.isEmpty() || filter.actionTypes.contains(log.actionType)) &&
            (filter.tableName == null || log.tableName == filter.tableName)
        }
        return filtered.drop(offset).take(limit)
    }

    fun getLogById(id: Int): AuditLog? {
        val log = mockLogs.find { it.id == id }
        return if (log != null) {
            AuditLog(
                id = log.id,
                actionType = log.actionType,
                tableName = log.tableName,
                objectId = -1,
                executorName = log.executorName,
                executorUserId = -1,
                eventTime = log.eventTime,
                ipAddress = log.ipAddress,
                dataChange = DataChange(emptyMap(), emptyMap()),
                userAgent = "MockAgent",
                requestMethod = "GET",
                origin = "Mock"
            )
        } else {
            // Возвращаем fallback вместо null
            AuditLog(
                id = id,
                actionType = ActionType.CREATE,
                tableName = "unknown",
                objectId = -1,
                executorName = "System",
                executorUserId = -1,
                eventTime = Instant.now(),
                ipAddress = "0.0.0.0",
                dataChange = DataChange(emptyMap(), emptyMap()),
                userAgent = "MockAgent",
                requestMethod = "GET",
                origin = "Mock"
            )
        }
    }

    fun getStatistics(): AuditStatistics {
        return AuditStatistics(
            totalEvents = 1250L,
            criticalEventsCount = 3L,
            todayEvents = 42L,
            activeSessions = 15
        )
    }
}