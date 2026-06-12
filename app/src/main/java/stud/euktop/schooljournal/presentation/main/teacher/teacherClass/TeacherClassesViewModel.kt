package stud.euktop.schooljournal.presentation.main.teacher.teacherClass

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.coordinator.TeacherCoordinator
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterTeacher
import javax.inject.Inject

@HiltViewModel
class TeacherClassesViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val teacherCoordinator: TeacherCoordinator,
    private val router: RouterTeacher,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<TeacherClassesState, Unit>() {

    override fun initState() = TeacherClassesState()

    init {
        executeCoordinator = coordinatorExec
        loadCurrentUser()
        loadClasses()
    }

    private fun loadCurrentUser() {
        executeWithLoadingSync(
            key = "load_user",
            block = { authRepository.getCurrentUser() },
            onSuccess = { user ->
                val fullName = "${user.firstName} ${user.lastName}".trim()
                _state.update { it.copy(teacherName = fullName) }
            }
        )
    }

    fun loadClasses() {
        executeLoadingBlockSync(
            key = "load_classes",
            block = {
                val currentUser = authRepository.getCurrentUser().getOrNull()
                return@executeLoadingBlockSync if (currentUser != null) {
                    teacherCoordinator.getTeacherClasses(currentUser.userId)
                } else {
                    CoordinatorResult.Success(emptyList())
                }
            },
            onSuccess = { classes ->
                _state.update { it.copy(classes = classes) }
            }
        )
    }

    fun onClassClick(item: TeacherClassItem) {
        router.toTeacherLessons(item.classId, item.subjectId)
    }
}