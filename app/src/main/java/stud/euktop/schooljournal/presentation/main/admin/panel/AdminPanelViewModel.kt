package stud.euktop.schooljournal.presentation.main.admin.panel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.ClassInfo
import stud.euktop.domain.model.Subject
import stud.euktop.domain.model.TeacherAssignment
import stud.euktop.domain.model.UserInfo
import stud.euktop.domain.repository.AdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

/**
 * ViewModel для административной панели.
 *
 * Назначение: загружает и хранит все справочные данные для администрирования.
 *
 * Функционал:
 * - State: users, classes, subjects, assignments (каждый List<...>), isLoading
 * - refreshAll() – параллельная загрузка через AdminRepository
 * - Обновление state после успешной загрузки
 * - Обработка ошибок частичной загрузки (если один из запросов упал)
 */
@Suppress("UNCHECKED_CAST")
@HiltViewModel
class AdminPanelViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager,
    private val adminRepository: AdminRepository
) : BaseViewModel<AdminPanelState, Unit>() {
    override fun initState() = AdminPanelState()

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        refreshAll()
    }

    fun refreshAll() {
        executeWithCoordinatorAndLoadingSync(
            block = {
                val users = adminRepository.getUsers()
                val classes = adminRepository.getClasses()
                val subjects = adminRepository.getSubjects()
                val assignments = adminRepository.getTeacherAssignments()
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
            block = { adminRepository.deleteUser(userId) },
            onSuccess = { refreshAll() }
        )
    }

    fun deleteClass(classId: Int) {
        executeWithCoordinatorAndLoadingSync(
            block = { adminRepository.deleteClass(classId) },
            onSuccess = { refreshAll() }
        )
    }
    fun deleteSubject(subjectId: Int) {
        executeWithCoordinatorAndLoadingSync(
            block = { adminRepository.deleteSubject(subjectId) },
            onSuccess = { refreshAll() }
        )
    }
}