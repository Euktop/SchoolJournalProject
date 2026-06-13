package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockAuditDataSource
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.audit.ActionType
import stud.euktop.domain.model.audit.AuditFilter
import stud.euktop.domain.model.audit.AuditLog
import stud.euktop.domain.model.audit.AuditLogListItem
import stud.euktop.domain.model.audit.AuditStatistics
import stud.euktop.domain.model.audit.DataChange
import stud.euktop.domain.repository.AuditLogRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuditMockLogRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler
) : AuditLogRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getLogs(
        filter: AuditFilter,
        limit: Int,
        offset: Int
    ): Result<List<AuditLogListItem>> = apiErrorHandler.safeApiCall {
        logger?.d(tag, "getLogs", "filter=$filter, limit=$limit, offset=$offset")
        MockDelayService.delay()
        val result = MockAuditDataSource.getLogs(filter, limit, offset)
        logger?.i(tag, "getLogs_success", "Returned ${result.size} logs")
        result.ifEmpty { emptyList() }
    }

    override suspend fun getLogById(id: Int): Result<AuditLog?> = apiErrorHandler.safeApiCall {
        logger?.d(tag, "getLogById", "id=$id")
        MockDelayService.delay()
        val log = MockAuditDataSource.getLogById(id)
        if (log != null) {
            logger?.i(tag, "getLogById_success", "Log found")
            log
        } else {
            logger?.d(
                tag,
                "getLogById_fallback",
                "Log not found, returning fallback object instead of null"
            )
            // Строго запрещаем возврат null, возвращаем валидный fallback-объект для безопасного тестирования UI
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

    override suspend fun getStatistics(): Result<AuditStatistics> = apiErrorHandler.safeApiCall {
        logger?.d(tag, "getStatistics", "Fetching audit statistics")
        MockDelayService.delay()
        val stats = MockAuditDataSource.getStatistics()
        logger?.i(
            tag,
            "getStatistics_success",
            "Stats: total=${stats.totalEvents}, today=${stats.todayEvents}"
        )
        stats
    }
}