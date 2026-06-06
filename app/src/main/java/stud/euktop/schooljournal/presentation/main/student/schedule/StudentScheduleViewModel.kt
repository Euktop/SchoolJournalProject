package stud.euktop.schooljournal.presentation.main.student.schedule

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class StudentScheduleViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<StudentScheduleState, Unit>() {

    override fun initState() = StudentScheduleState()

    init {
        executeCoordinator = coordinatorExec
        loadSchedule()
    }

    fun loadSchedule() {
        executeWithLoadingSync(
            key = "load_schedule",
            block = { studentRepository.getStudentSchedule() }
        ) { items ->
            _state.update { it.copy(schedule = items) }
        }
    }
}