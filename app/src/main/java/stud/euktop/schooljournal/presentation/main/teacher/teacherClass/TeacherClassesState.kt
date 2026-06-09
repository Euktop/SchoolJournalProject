package stud.euktop.schooljournal.presentation.main.teacher.teacherClass

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class TeacherClassesState(
    val classes: List<TeacherClassItem> = emptyList(),
    val teacherName: String = "",
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<TeacherClassesState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): TeacherClassesState =
        copy(loadingMap = loadingMap)
}