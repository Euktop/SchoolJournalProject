package stud.euktop.schooljournal.presentation.main.profile

import stud.euktop.domain.model.user.UserProfile
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class ProfileState(
    override val isLoading: Boolean = false,
    val user: UserProfile? = null
) : BaseState<ProfileState>() {
    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}