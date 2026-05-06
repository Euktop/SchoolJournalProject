package stud.euktop.schooljournal.presentation.main.student.homework

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import stud.euktop.domain.model.school.Subject
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

    fun loadSubjects() {
        execSync({ subjectAdminRepository.getSubjects() }) { subjects ->
            _subjects.value = subjects
        }
    }
}