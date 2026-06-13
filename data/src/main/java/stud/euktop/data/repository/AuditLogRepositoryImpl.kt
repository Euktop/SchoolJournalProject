package stud.euktop.data.repository

import com.schooljournal.api.AuditApi
import stud.euktop.data.map.toDomain
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.audit.AuditFilter
import stud.euktop.domain.model.audit.AuditLog
import stud.euktop.domain.model.audit.AuditLogListItem
import stud.euktop.domain.model.audit.AuditStatistics
import stud.euktop.domain.repository.AuditLogRepository
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuditLogRepositoryImpl @Inject constructor(
    private val auditApi: AuditApi,
    private val errorHandler: ApiErrorHandler
) : AuditLogRepository {

    override suspend fun getLogs(
        filter: AuditFilter,
        limit: Int,
        offset: Int
    ): Result<List<AuditLogListItem>> =
        errorHandler.safeApiCall {
            auditApi.apiAuditGet(
                userId = filter.userQuery?.toIntOrNull(),
                filterByUserId = filter.userQuery?.toIntOrNull() != null,
                targetTable = filter.tableName,
                filterByTargetTable = !filter.tableName.isNullOrEmpty(),
                actionType = filter.actionTypes.joinToString(","),
                filterByActionType = filter.actionTypes.isNotEmpty(),
                dateFrom = filter.fromDate?.toInstant()?.atZone(ZoneId.systemDefault())
                    ?.toLocalDateTime(),
                filterByDateFrom = filter.fromDate != null,
                dateTo = filter.toDate?.toInstant()?.atZone(ZoneId.systemDefault())
                    ?.toLocalDateTime(),
                filterByDateTo = filter.toDate != null,
                offset = offset,
                limit = limit
            ).map { dto ->
                AuditLogListItem(
                    id = dto.auditLogId?.toInt() ?: 0,
                    actionType = stud.euktop.domain.model.audit.ActionType.fromString(
                        dto.actionType ?: ""
                    ) ?: stud.euktop.domain.model.audit.ActionType.CREATE,
                    tableName = dto.targetTable ?: "",
                    executorName = "User_${dto.userId}",
                    ipAddress = dto.ipAddress ?: "",
                    eventTime = dto.timestamp?.toInstant(ZoneOffset.UTC) ?: Instant.now()
                )
            }
        }

    override suspend fun getLogById(id: Int): Result<AuditLog?> = errorHandler.safeApiCall {
        auditApi.apiAuditIdGet(id).toDomain()
    }

    override suspend fun getStatistics(): Result<AuditStatistics> = errorHandler.safeApiCall {
        auditApi.apiAuditStatisticsGet().toDomain()
    }
}