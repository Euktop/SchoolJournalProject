package stud.euktop.schooljournal.presentation.main.admin.users

import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel

data class UserEditState(override val loadingMap: Map<String, Boolean> = emptyMap()) :
    BaseState<UserEditState>() {
    override fun updateLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}

class UserEditViewModel : BaseViewModel<UserEditState, Unit>() {
    override fun initState() = UserEditState()
}