package stud.euktop.schooljournal.presentation.main.admin.assignments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.model.assignment.TeacherAssignment
import stud.euktop.domain.model.assignment.TeacherAssignmentUpdate
import stud.euktop.domain.model.common.Field
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.ClassInfoFilter
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.coordinator.AdminCoordinator
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TeacherAssignmentEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val coordinator: AdminCoordinator,
    private val routerAdmin: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<TeacherAssignmentEditState, Unit>() {
    init {
        executeCoordinator = coordinatorExec
    }

    private val assignmentId = savedStateHandle.get<AssignmentId>("assignmentId")
    private val isEditMode get() = assignmentId != null

    val teacherFilterFlow = MutableStateFlow(UserFilter(role = Role.TEACHER))
    val classFilterFlow = MutableStateFlow(ClassInfoFilter())
    val subjectFilterFlow = MutableStateFlow(SubjectFilter())

    override fun initState() = TeacherAssignmentEditState(assignmentId = assignmentId)

    init {
        if (isEditMode) loadAssignment()
    }

    private fun loadAssignment() {
        executeLoadingBlockSync(
            key = "load",
            block = { coordinator.getTeacherAssignmentFull(assignmentId!!) },
            onSuccess = { assignment ->
                _state.update {
                    it.copy(
                        teacher = assignment.teacher,
                        classInfo = assignment.classInfo,
                        subject = assignment.subject,
                        validFrom = assignment.assignmentId.validFrom,
                        validTo = assignment.validToDate,
                        isPrimary = assignment.isPrimary,
                        originalTeacherId = assignment.teacher?.userId,
                        originalClassId = assignment.classInfo?.classId,
                        originalSubjectId = assignment.subject?.subjectId,
                        originalValidFrom = assignment.assignmentId.validFrom,
                        originalValidTo = assignment.validToDate,
                        originalIsPrimary = assignment.isPrimary
                    )
                }
            }
        )
    }

    fun getTeachersPagingDataFlow(filter: UserFilter): Flow<PagingData<UserProfile>> =
        coordinator.getUsersPagingDataFlow(filter)

    fun getClassesPagingDataFlow(filter: ClassInfoFilter): Flow<PagingData<ClassInfo>> =
        coordinator.getClassesPagingDataFlow(filter)

    fun getSubjectsPagingDataFlow(filter: SubjectFilter): Flow<PagingData<Subject>> =
        coordinator.getSubjectsPagingDataFlow(filter)

    fun updateTeacher(teacher: UserProfile?) {
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
        if (isEditMode) {
            val update = TeacherAssignmentUpdate(
                id = state.assignmentId!!,
                validToDate = Field(state.validTo, state.validTo != state.originalValidTo),
                isPrimary = Field(state.isPrimary, state.isPrimary != state.originalIsPrimary)
            )
            executeLoadingBlockSync(
                key = "save",
                block = { coordinator.updateTeacherAssignment(update) },
                onSuccess = { routerAdmin.toBack() }
            )
        } else {
            val assignment = TeacherAssignment(
                assignmentId = AssignmentId(
                    teacherId = state.teacher!!.userId,
                    classId = state.classInfo!!.classId,
                    subjectId = state.subject!!.subjectId,
                    validFrom = state.validFrom!!
                ),
                validToDate = state.validTo,
                isPrimary = state.isPrimary
            )
            executeLoadingBlockSync(
                key = "save",
                block = { coordinator.addTeacherAssignment(assignment) },
                onSuccess = { routerAdmin.toBack() }
            )
        }
    }

    fun cancel() {
        viewModelScope.launch {
            routerAdmin.toBack()
        }
    }
}