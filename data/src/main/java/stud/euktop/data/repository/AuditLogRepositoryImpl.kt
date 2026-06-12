package stud.euktop.data.repository

import stud.euktop.domain.model.audit.AuditFilter
import stud.euktop.domain.model.audit.AuditLog
import stud.euktop.domain.model.audit.AuditLogListItem
import stud.euktop.domain.model.audit.AuditStatistics
import stud.euktop.domain.repository.AuditLogRepository
import javax.inject.Inject

class AuditLogRepositoryImpl @Inject constructor() : AuditLogRepository {
    override suspend fun getLogs(
        filter: AuditFilter,
        limit: Int,
        offset: Int
    ): Result<List<AuditLogListItem>> {
        TODO()
    }

    override suspend fun getLogById(id: Int): Result<AuditLog?> {
        TODO("Not yet implemented")
    }

    override suspend fun getStatistics(): Result<AuditStatistics> {
        TODO("Not yet implemented")
    }
}