package stud.euktop.schooljournal.presentation.main.student.studentSubjects

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.coordinator.StudentCoordinator
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

// presentation/main/student/studentSubjects/StudentSubjectsViewModel.kt
@HiltViewModel
class StudentSubjectsViewModel @Inject constructor(
    private val studentCoordinator: StudentCoordinator,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<StudentSubjectsState, Unit>() {

    override fun initState() = StudentSubjectsState()

    init {
        executeCoordinator = coordinatorExec
        loadData()
    }

    private fun loadData() {
        executeLoadingBlockSync(
            key = "load_subjects",
            block = { studentCoordinator.getSubjectsSummary() },
            onSuccess = { summaries ->
                _state.update { it.copy(subjects = summaries) }
            }
        )

        executeLoadingBlockSync(
            key = "load_overall",
            block = { studentCoordinator.getOverallAverage() },
            onSuccess = { overall ->
                _state.update {
                    it.copy(
                        overallAverage = overall.averageMark,
                        totalGrades = overall.totalGrades
                    )
                }
            }
        )
    }
}