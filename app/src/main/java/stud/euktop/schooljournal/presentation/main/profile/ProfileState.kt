package stud.euktop.schooljournal.presentation.main.profile

import stud.euktop.domain.model.user.UserProfile
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class ProfileState(
    val user: UserProfile? = null,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<ProfileState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): ProfileState {
        return copy(loadingMap = loadingMap)
    }
}