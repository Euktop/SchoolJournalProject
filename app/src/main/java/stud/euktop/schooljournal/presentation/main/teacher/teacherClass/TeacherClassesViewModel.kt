package stud.euktop.schooljournal.presentation.main.teacher.teacherClass

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.TeacherRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

@HiltViewModel
class TeacherClassesViewModel @Inject constructor(
    private val repository: TeacherRepository,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager
) : BaseViewModel<TeacherClassesState, Unit>() {
    override fun initState() = TeacherClassesState()

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        loadClasses()
    }

    fun loadClasses() {
        executeWithCoordinatorAndLoadingSync(
            block = {
                repository.getTeacherClasses()
            }, { classes ->
                _state.update { it.copy(classes = classes) }
            }
        )
    }
}