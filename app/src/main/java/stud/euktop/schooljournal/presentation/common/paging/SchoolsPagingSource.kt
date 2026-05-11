package stud.euktop.schooljournal.presentation.common.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.repository.SchoolAdminRepository

class SchoolsPagingSource(
    private val repository: SchoolAdminRepository,
    private val filter: SchoolFilter,
    private val pageSize: Int = 20
) : PagingSource<Int, School>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, School> {
        val offset = params.key ?: 0
        return try {
            val paginatedFilter = filter.copy(pagination = Pagination(offset, pageSize))
            val schools = repository.getSchools(paginatedFilter).getOrElse { emptyList() }
            LoadResult.Page(
                data = schools,
                prevKey = if (offset > 0) offset - pageSize else null,
                nextKey = if (schools.size == pageSize) offset + pageSize else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, School>): Int? {
        val anchor = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchor)
        return closestPage?.prevKey?.plus(pageSize) ?: closestPage?.nextKey?.minus(pageSize)
    }
}