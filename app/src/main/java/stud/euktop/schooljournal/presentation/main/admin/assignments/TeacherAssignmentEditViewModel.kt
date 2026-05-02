package stud.euktop.schooljournal.presentation.main.admin.assignments

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.assignment.TeacherAssignment
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.repository.*
import stud.euktop.uikit.R as R
import stud.euktop.schooljournal.R as R2
import stud.euktop.schooljournal.presentation.common.message.MessageEvent
import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.main.admin.common.base.BaseEditViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TeacherAssignmentEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager,
    private val userAdminRepository: UserAdminRepository,
    private val classAdminRepository: ClassAdminRepository,
    private val subjectAdminRepository: SubjectAdminRepository,
    private val assignmentAdminRepository: AssignmentAdminRepository
) : BaseEditViewModel<TeacherAssignmentEditState>(coordinatorExec, navigationManager) {

    companion object {
        const val KEY_ASSIGNMENT_ID = "assignmentId"
    }

    private val assignmentId: Int = savedStateHandle[KEY_ASSIGNMENT_ID] ?: 0

    init {
        loadInitialData()
        if (assignmentId != 0) loadAssignment()
    }

    override fun initState() = TeacherAssignmentEditState(assignmentId = assignmentId)

    private fun loadInitialData() {
        executeLoadingBlockSync {
            val teachersDeferred = async { userAdminRepository.getTeachersByRole(Role.TEACHER) }
            val classesDeferred = async { classAdminRepository.getClasses() }
            val subjectsDeferred = async { subjectAdminRepository.getSubjects() }

            teachersDeferred.await().onSuccess { teachers ->
                _state.update { it.copy(availableTeachers = teachers) }
            }.onFailure {
                _messageEvent.tryEmit(MessageEvent.Message(MessageParam(R2.string.error_load_teachers)))
            }

            classesDeferred.await().onSuccess { classes ->
                _state.update { it.copy(availableClasses = classes) }
            }.onFailure {
                _messageEvent.tryEmit(MessageEvent.Message(MessageParam(R.string.error_load_classes)))
            }

            subjectsDeferred.await().onSuccess { subjects ->
                _state.update { it.copy(availableSubjects = subjects) }
            }.onFailure {
                _messageEvent.tryEmit(MessageEvent.Message(MessageParam(R.string.error_load_subjects)))
            }
        }
    }

    private fun loadAssignment() {
        executeWithCoordinatorAndLoadingSync(
            block = { assignmentAdminRepository.getTeacherAssignment(assignmentId) },
            onSuccess = { assignment ->
                _state.update {
                    it.copy(
                        teacher = assignment.teacher,
                        classInfo = assignment.classInfo,
                        subject = assignment.subject,
                        validFrom = assignment.validFromDate,
                        validTo = assignment.validToDate,
                        isPrimary = assignment.isPrimary
                    )
                }
            }
        )
    }

    fun updateTeacher(teacher: UserInfo?) {
        _state.update { it.copy(teacher = teacher) }
    }

    fun updateClass(classInfo: ClassInfo?) {
        _state.update { it.copy(classInfo = classInfo) }
    }

    fun updateSubject(subject: Subject?) {
        _state.update { it.copy(subject = subject) }
    }

    fun updateValidFrom(date: Date?) {
        _state.update { it.copy(validFrom = date) }
    }

    fun updateValidTo(date: Date?) {
        _state.update { it.copy(validTo = date) }
    }

    fun updateIsPrimary(isPrimary: Boolean) {
        _state.update { it.copy(isPrimary = isPrimary) }
    }

    fun save() {
        val state = _state.value
        if (!state.isFormValid()) return

        val assignment = TeacherAssignment(
            id = state.assignmentId,
            teacher = state.teacher!!,
            classInfo = state.classInfo!!,
            subject = state.subject!!,
            validFromDate = state.validFrom!!,
            validToDate = state.validTo,
            isPrimary = state.isPrimary
        )

        executeWithCoordinatorAndLoadingSync(
            block = {
                if (state.isEditMode()) assignmentAdminRepository.updateTeacherAssignment(assignment)
                else assignmentAdminRepository.addTeacherAssignment(assignment)
            },
            onSuccess = { navigateBack() }
        )
    }
    // presentation/main/admin/assignments/TeacherAssignmentEditViewModel.kt

    fun loadTeachers(query: String) {
        executeWithCoordinatorAndLoadingSync(
            block = { userAdminRepository.getTeachersByRole(Role.TEACHER) },
            onSuccess = { teachers ->
                // Временная локальная фильтрация для демонстрации (позже заменить на серверный поиск)
                val filtered = if (query.isBlank()) teachers
                else teachers.filter { "${it.lastName} ${it.firstName}".contains(query, ignoreCase = true) }
                _state.update { it.copy(availableTeachers = filtered) }
            }
        )
    }

    fun loadClasses(query: String) {
        executeWithCoordinatorAndLoadingSync(
            block = { classAdminRepository.getClasses() },
            onSuccess = { classes ->
                val filtered = if (query.isBlank()) classes
                else classes.filter { "${it.grade}${it.letter} (${it.school.name})".contains(query, ignoreCase = true) }
                _state.update { it.copy(availableClasses = filtered) }
            }
        )
    }

    fun loadSubjects(query: String) {
        executeWithCoordinatorAndLoadingSync(
            block = { subjectAdminRepository.getSubjects() },
            onSuccess = { subjects ->
                val filtered = if (query.isBlank()) subjects
                else subjects.filter { it.name.contains(query, ignoreCase = true) }
                _state.update { it.copy(availableSubjects = filtered) }
            }
        )
    }
}