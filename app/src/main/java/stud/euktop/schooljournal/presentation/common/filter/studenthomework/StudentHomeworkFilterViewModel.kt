package stud.euktop.schooljournal.presentation.common.filter.studenthomework

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.repository.SubjectAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseFilterViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class StudentHomeworkFilterViewModel @Inject constructor(
    private val subjectAdminRepository: SubjectAdminRepository,
    coordinatorExec: CoordinatorExec
) : BaseFilterViewModel(coordinatorExec) {

    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects

    fun loadSubjects(filter: SubjectFilter = SubjectFilter()) {
        execSync(
            { subjectAdminRepository.getSubjects(filter) },
            { subjects -> _subjects.update { subjects } }
        )
    }
}