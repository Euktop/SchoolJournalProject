package stud.euktop.schooljournal.presentation.main.profile

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class ProfileState(
    override val isLoading: Boolean = false,
    val userName: String = "",
    val email: String = "",
    val roleNames: List<String> = emptyList()
) : BaseState<ProfileState>() {
    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}

