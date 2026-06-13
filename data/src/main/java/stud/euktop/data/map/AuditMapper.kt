package stud.euktop.data.map

import com.schooljournal.model.GetAuditLogByIdResult
import com.schooljournal.model.GetAuditStatisticsResult
import org.openapitools.client.infrastructure.Serializer
import stud.euktop.domain.model.audit.ActionType
import stud.euktop.domain.model.audit.AuditLog
import stud.euktop.domain.model.audit.AuditStatistics
import stud.euktop.domain.model.audit.DataChange
import java.time.Instant
import java.time.ZoneOffset

internal fun GetAuditLogByIdResult.toDomain(): AuditLog {
    return AuditLog(
        id = (this.id ?: 0).toInt(),
        actionType = ActionType.fromString(this.actionType ?: "") ?: ActionType.CREATE,
        tableName = this.targetTable ?: "",
        objectId = this.recordId ?: 0,
        executorName = this.userName ?: "",
        executorUserId = this.userId ?: 0,
        eventTime = this.createdAt?.toInstant(ZoneOffset.UTC) ?: Instant.now(),
        ipAddress = this.ipAddress ?: "",
        dataChange = DataChange(
            oldValue = parseJsonToMap(this.oldValues),
            newValue = parseJsonToMap(this.newValues)
        ),
        userAgent = this.userAgent ?: "",
        requestMethod = "",
        origin = ""
    )
}

internal fun GetAuditStatisticsResult.toDomain(): AuditStatistics {
    return AuditStatistics(
        totalEvents = (this.totalEvents ?: 0).toLong(),
        criticalEventsCount = (this.criticalEvents ?: 0).toLong(),
        todayEvents = (this.todayEvents ?: 0).toLong(),
        activeSessions = 0
    )
}

private fun parseJsonToMap(jsonString: String?): Map<String, Any?> {
    return try {
        jsonString?.let {
            Serializer.moshi.adapter(Map::class.java).fromJson(it) as? Map<String, Any?>
        } ?: emptyMap()
    } catch (e: Exception) {
        emptyMap()
    }
}