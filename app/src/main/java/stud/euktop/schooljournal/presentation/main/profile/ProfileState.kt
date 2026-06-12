package stud.euktop.schooljournal.presentation.main.profile

import android.net.Uri
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class ProfileState(
    val user: UserProfile? = null,
    val role: Role? = null,
    val school: School? = null,
    val avatarUri: Uri? = null,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<ProfileState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): ProfileState {
        return copy(loadingMap = loadingMap)
    }
}