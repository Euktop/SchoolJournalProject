package stud.euktop.schooljournal.presentation.common.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.repository.StudentRepository
import java.util.Date

class StudentMarksPagingSource(
    private val repository: StudentRepository,
    private val subjectId: Int,
    private val studentId: Int?,
    private val startDate: Date?,
    private val endDate: Date?,
    private val pageSize: Int = 20
) : PagingSource<Int, StudentSubjectMark>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StudentSubjectMark> {
        val offset = params.key ?: 0
        return try {
            val marks = repository.getSubjectMarks(
                subjectId = subjectId,
                studentId = studentId,
                startDate = startDate,
                endDate = endDate,
                offset = offset,
                limit = pageSize
            ).getOrElse { emptyList() }
            LoadResult.Page(
                data = marks,
                prevKey = if (offset > 0) offset - pageSize else null,
                nextKey = if (marks.size == pageSize) offset + pageSize else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StudentSubjectMark>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(pageSize)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(pageSize)
        }
}