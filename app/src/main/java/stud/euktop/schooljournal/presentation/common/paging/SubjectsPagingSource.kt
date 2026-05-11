package stud.euktop.schooljournal.presentation.common.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.repository.SubjectAdminRepository

class SubjectsPagingSource(
    private val repository: SubjectAdminRepository,
    private val filter: SubjectFilter,
    private val pageSize: Int = 20
) : PagingSource<Int, Subject>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Subject> {
        val offset = params.key ?: 0
        return try {
            val paginatedFilter = filter.copy(pagination = Pagination(offset, pageSize))
            val subjects = repository.getSubjects(paginatedFilter).getOrElse { emptyList() }
            LoadResult.Page(
                data = subjects,
                prevKey = if (offset > 0) offset - pageSize else null,
                nextKey = if (subjects.size == pageSize) offset + pageSize else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Subject>): Int? {
        val anchor = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchor)
        return closestPage?.prevKey?.plus(pageSize) ?: closestPage?.nextKey?.minus(pageSize)
    }
}