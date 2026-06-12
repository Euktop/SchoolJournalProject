package stud.euktop.domain.repository

import stud.euktop.domain.model.audit.AuditFilter
import stud.euktop.domain.model.audit.AuditLog
import stud.euktop.domain.model.audit.AuditLogListItem
import stud.euktop.domain.model.audit.AuditStatistics

interface AuditLogRepository {
    /**
     * Получить список логов с фильтрацией и пагинацией
     * @param filter критерии фильтрации
     * @param limit количество записей
     * @param offset сдвиг (для пагинации)
     * @return список элементов для отображения в списке
     */
    suspend fun getLogs(
        filter: AuditFilter,
        limit: Int = 20,
        offset: Int = 0
    ): Result<List<AuditLogListItem>>

    /**
     * Получить полную информацию о логе по ID
     * @param id идентификатор лога
     * @return полная модель или null, если не найдена
     */
    suspend fun getLogById(id: Int): Result<AuditLog?>

    /**
     * Получить общую статистику (например, для верхних карточек на экране списка)
     */
    suspend fun getStatistics(): Result<AuditStatistics>
}