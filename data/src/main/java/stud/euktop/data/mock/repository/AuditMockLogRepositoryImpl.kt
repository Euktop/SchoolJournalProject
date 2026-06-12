package stud.euktop.data.mock.repository

import stud.euktop.domain.model.audit.ActionType
import stud.euktop.domain.model.audit.AuditFilter
import stud.euktop.domain.model.audit.AuditLog
import stud.euktop.domain.model.audit.AuditLogListItem
import stud.euktop.domain.model.audit.AuditStatistics
import stud.euktop.domain.repository.AuditLogRepository
import java.time.Instant
import javax.inject.Inject

class AuditMockLogRepositoryImpl @Inject constructor() : AuditLogRepository {
    override suspend fun getLogs(
        filter: AuditFilter,
        limit: Int,
        offset: Int
    ): Result<List<AuditLogListItem>> {
        return Result.success(
            (1..20).map { index ->
                AuditLogListItem(
                    id = index,
                    actionType = ActionType.CREATE,
                    tableName = "users",
                    executorName = "Admin",
                    ipAddress = "127.0.0.1",
                    eventTime = Instant.now()
                )
            }
        )
    }

    override suspend fun getLogById(id: Int): Result<AuditLog?> {
        TODO("Not yet implemented")
    }

    override suspend fun getStatistics(): Result<AuditStatistics> {
        TODO("Not yet implemented")
    }
}