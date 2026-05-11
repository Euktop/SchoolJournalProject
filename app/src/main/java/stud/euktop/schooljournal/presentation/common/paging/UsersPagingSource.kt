package stud.euktop.schooljournal.presentation.common.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import stud.euktop.domain.model.common.Pagination
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.repository.UserAdminRepository

class UsersPagingSource(
    private val repository: UserAdminRepository,
    private val filter: UserFilter,
    private val pageSize: Int = 20,
    private val loadFullProfile: Boolean = true
) : PagingSource<Int, UserProfile>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserProfile> {
        val offset = params.key ?: 0
        return try {
            val paginatedFilter = filter.copy(pagination = Pagination(offset, pageSize))
            val users = repository.getUsers(paginatedFilter).getOrElse { emptyList() }
            val profiles = if (loadFullProfile) {
                users.mapNotNull { repository.getUser(it.userId).getOrNull() }
            } else {
                users.map { it.toUserProfile() }
            }
            LoadResult.Page(
                data = profiles,
                prevKey = if (offset > 0) offset - pageSize else null,
                nextKey = if (profiles.size == pageSize) offset + pageSize else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserProfile>): Int? {
        val anchor = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchor)
        return closestPage?.prevKey?.plus(pageSize) ?: closestPage?.nextKey?.minus(pageSize)
    }
}

fun UserListItem.toUserProfile(): UserProfile = UserProfile.createObject(
    userId = userId,
    lastName = lastName,
    firstName = firstName,
    surName = surName,
    email = email,
    accountStatus = accountStatus
)