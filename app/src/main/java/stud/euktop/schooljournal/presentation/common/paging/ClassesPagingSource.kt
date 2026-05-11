package stud.euktop.schooljournal.presentation.common.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.ClassInfoFilter
import stud.euktop.domain.repository.ClassAdminRepository

class ClassesPagingSource(
    private val repository: ClassAdminRepository,
    private val filter: ClassInfoFilter,
    private val pageSize: Int = 20
) : PagingSource<Int, ClassInfo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ClassInfo> {
        val offset = params.key ?: 0
        return try {
            val paginatedFilter = filter.copy(pagination = Pagination(offset, pageSize))
            val classes = repository.getClasses(paginatedFilter).getOrElse { emptyList() }
            LoadResult.Page(
                data = classes,
                prevKey = if (offset > 0) offset - pageSize else null,
                nextKey = if (classes.size == pageSize) offset + pageSize else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ClassInfo>): Int? {
        val anchor = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchor)
        return closestPage?.prevKey?.plus(pageSize) ?: closestPage?.nextKey?.minus(pageSize)
    }
}