package stud.euktop.schooljournal.presentation.main.teacher.home

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class TeacherHomeState(
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<TeacherHomeState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): TeacherHomeState = 
        copy(loadingMap = loadingMap)
}
