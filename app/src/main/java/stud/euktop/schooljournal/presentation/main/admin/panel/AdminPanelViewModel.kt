package stud.euktop.schooljournal.presentation.main.admin.panel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.assignment.TeacherAssignment
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.repository.*
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

@HiltViewModel
class AdminPanelViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager,
    private val userAdminRepository: UserAdminRepository,
    private val classAdminRepository: ClassAdminRepository,
    private val subjectAdminRepository: SubjectAdminRepository,
    private val assignmentAdminRepository: AssignmentAdminRepository
) : BaseViewModel<AdminPanelState, Unit>() {

    override fun initState() = AdminPanelState()

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        refreshAll()
    }

    fun refreshAll() {
        executeWithCoordinatorAndLoadingSync(
            block = {
                val users = userAdminRepository.getUsers()
                val classes = classAdminRepository.getClasses()
                val subjects = subjectAdminRepository.getSubjects()
                val assignments = assignmentAdminRepository.getTeacherAssignments()
                if (users.isFailure || classes.isFailure || subjects.isFailure || assignments.isFailure)
                    Result.failure(Exception("Load failed"))
                else Result.success(
                    listOf(
                        users.getOrThrow(),
                        classes.getOrThrow(),
                        subjects.getOrThrow(),
                        assignments.getOrThrow()
                    )
                )
            },
            onSuccess = { lists ->
                _state.update {
                    it.copy(
                        users = lists[0] as List<UserInfo>,
                        classes = lists[1] as List<ClassInfo>,
                        subjects = lists[2] as List<Subject>,
                        assignments = lists[3] as List<TeacherAssignment>
                    )
                }
            }
        )
    }

    fun deleteUser(userId: Int) {
        executeWithCoordinatorAndLoadingSync(
            block = { userAdminRepository.deleteUser(userId) },
            onSuccess = { refreshAll() }
        )
    }

    fun deleteClass(classId: Int) {
        executeWithCoordinatorAndLoadingSync(
            block = { classAdminRepository.deleteClass(classId) },
            onSuccess = { refreshAll() }
        )
    }

    fun deleteSubject(subjectId: Int) {
        executeWithCoordinatorAndLoadingSync(
            block = { subjectAdminRepository.deleteSubject(subjectId) },
            onSuccess = { refreshAll() }
        )
    }

    fun deleteAssignment(assignmentId: Int) {
        executeWithCoordinatorAndLoadingSync(
            block = { assignmentAdminRepository.deleteTeacherAssignment(assignmentId) },
            onSuccess = { refreshAll() }
        )
    }
}