package stud.euktop.schooljournal.presentation.main.admin.home

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class AdminHomeState(
    val adminName: String = "",
    val adminEmail: String = "",
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<AdminHomeState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}