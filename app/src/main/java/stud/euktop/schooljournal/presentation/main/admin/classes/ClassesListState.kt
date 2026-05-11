package stud.euktop.schooljournal.presentation.main.admin.classes

import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.filter.classes.AppClassInfoFilter

data class ClassesListState(
    val classes: List<ClassInfo> = emptyList(),
    val filter: AppClassInfoFilter = AppClassInfoFilter(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<ClassesListState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}