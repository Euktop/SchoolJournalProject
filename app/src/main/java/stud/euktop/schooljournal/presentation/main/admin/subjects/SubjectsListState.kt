package stud.euktop.schooljournal.presentation.main.admin.subjects

import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class SubjectsListState(
    val subjects: List<Subject> = emptyList(),
    val filter: SubjectFilter = SubjectFilter(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<SubjectsListState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}