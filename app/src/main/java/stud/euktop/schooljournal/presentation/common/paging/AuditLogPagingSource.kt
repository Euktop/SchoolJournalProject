package stud.euktop.schooljournal.presentation.common.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import stud.euktop.domain.model.audit.AuditFilter
import stud.euktop.domain.model.audit.AuditLogListItem
import stud.euktop.domain.repository.AuditLogRepository

class AuditLogPagingSource(
    private val repository: AuditLogRepository,
    private val filter: AuditFilter,
    private val pageSize: Int = 20
) : PagingSource<Int, AuditLogListItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AuditLogListItem> {
        val offset = params.key ?: 0
        return try {
            val items = repository.getLogs(filter, pageSize, offset)
                .getOrNull() ?: emptyList()
            LoadResult.Page(
                data = items,
                prevKey = if (offset > 0) offset - pageSize else null,
                nextKey = if (items.size == pageSize) offset + pageSize else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AuditLogListItem>): Int? {
        val anchor = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchor)
        return closestPage?.prevKey?.plus(pageSize) ?: closestPage?.nextKey?.minus(pageSize)
    }
}