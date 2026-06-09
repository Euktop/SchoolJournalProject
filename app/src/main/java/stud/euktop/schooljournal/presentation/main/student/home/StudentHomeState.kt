package stud.euktop.schooljournal.presentation.main.student.home

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class StudentHomeState(
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<StudentHomeState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): StudentHomeState =
        copy(loadingMap = loadingMap)
}

