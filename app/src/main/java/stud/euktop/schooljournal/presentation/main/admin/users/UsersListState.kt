package stud.euktop.schooljournal.presentation.main.admin.users

import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class UsersListState(
    val users: List<UserListItem> = emptyList(),
    val filter: UserFilter = UserFilter(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<UsersListState>() {
    override fun updateLoading(loadingMap: Map<String, Boolean>): UsersListState = copy(loadingMap = loadingMap)
}