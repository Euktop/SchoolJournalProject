package stud.euktop.schooljournal.presentation.main.admin.home

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class AdminHomeState(
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<AdminHomeState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): AdminHomeState =
        copy(loadingMap = loadingMap)
}

