package stud.euktop.schooljournal.presentation.main.admin.schools

import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class SchoolsListState(
    val schools: List<School> = emptyList(),
    val filter: SchoolFilter = SchoolFilter(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<SchoolsListState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}