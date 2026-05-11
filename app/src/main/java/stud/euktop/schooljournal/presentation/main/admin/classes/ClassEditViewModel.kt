package stud.euktop.schooljournal.presentation.main.admin.classes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.common.Field
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.ClassInfoUpdate
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.repository.ClassAdminRepository
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.contract.action.ClassFormActions
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import stud.euktop.schooljournal.presentation.common.paging.SchoolsPagingSource
import stud.euktop.schooljournal.presentation.common.paging.UsersPagingSource
import javax.inject.Inject

@HiltViewModel
class ClassEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val classRepository: ClassAdminRepository,
    private val schoolRepository: SchoolAdminRepository,
    private val userRepository: UserAdminRepository,
    private val routerAdmin: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<ClassEditState, Unit>(), ClassFormActions {

    private val classId: Int = savedStateHandle["classId"] ?: 0
    private val isEditMode get() = classId != 0

    override fun initState() = ClassEditState()

    init {
        executeCoordinator = coordinatorExec
        if (isEditMode) loadClass()
    }

    private fun loadClass() {
        withLoadingSync("load") {
            val classInfo = executeCoordinatorResult { classRepository.getClass(classId) }.await()
                ?: return@withLoadingSync
            val school =
                executeCoordinatorResult { schoolRepository.getSchool(classInfo.schoolId) }.await()
            val teacher =
                classInfo.teacherId?.let { executeCoordinatorResult { userRepository.getUser(it) }.await() }
            _state.update {
                it.copy(
                    grade = classInfo.grade,
                    letter = it.letter.copy(classInfo.letter),
                    academicYearStart = classInfo.academicYearStart,
                    academicYearEnd = classInfo.academicYearEnd,
                    school = school,
                    classTeacher = teacher,
                    originalGrade = classInfo.grade,
                    originalLetter = classInfo.letter,
                    originalYearStart = classInfo.academicYearStart,
                    originalYearEnd = classInfo.academicYearEnd,
                    originalSchoolId = classInfo.schoolId,
                    originalClassTeacherId = teacher?.userId
                )
            }
        }
    }

    fun getSchoolsPagingDataFlow(filter: SchoolFilter): Flow<PagingData<School>> {
        return Pager(PagingConfig(pageSize = 20)) { SchoolsPagingSource(schoolRepository, filter) }
            .flow.cachedIn(viewModelScope)
    }

    fun getTeachersPagingDataFlow(filter: UserFilter): Flow<PagingData<UserProfile>> {
        val teacherFilter = filter.copy(role = Role.TEACHER)
        return Pager(PagingConfig(pageSize = 20)) {
            UsersPagingSource(
                userRepository,
                teacherFilter
            )
        }
            .flow.cachedIn(viewModelScope)
    }

    override fun updateGrade(value: Int?) {
        _state.update { it.copy(grade = value) }
    }

    override fun updateLetter(value: String) {
        _state.update { it.copy(letter = it.letter.copy(value)) }
    }

    override fun updateAcademicYearStart(year: Int?) {
        _state.update { it.copy(academicYearStart = year) }
    }

    override fun updateAcademicYearEnd(year: Int?) {
        _state.update { it.copy(academicYearEnd = year) }
    }

    override fun updateSchool(school: School?) {
        _state.update { it.copy(school = school) }
    }

    override fun updateClassTeacher(teacher: UserProfile?) {
        _state.update { it.copy(classTeacher = teacher) }
    }

    fun save() {
        val state = _state.value
        if (!state.isFormValid()) return
        if (isEditMode) {
            val update = ClassInfoUpdate(
                classId = classId,
                schoolId = Field(
                    state.school?.schoolId,
                    state.school?.schoolId != state.originalSchoolId
                ),
                grade = Field(state.grade, state.grade != state.originalGrade),
                letter = Field(
                    state.letter.getValidate(),
                    state.letter.value != state.originalLetter
                ),
                academicYearStart = Field(
                    state.academicYearStart,
                    state.academicYearStart != state.originalYearStart
                ),
                academicYearEnd = Field(
                    state.academicYearEnd,
                    state.academicYearEnd != state.originalYearEnd
                ),
                teacherId = Field(
                    state.classTeacher?.userId,
                    state.classTeacher?.userId != state.originalClassTeacherId
                )
            )
            executeWithLoadingSync(
                "save",
                { classRepository.updateClass(update) }) { routerAdmin.navigateBack() }
        } else {
            val school = state.school ?: return
            val newClass = ClassInfo(
                classId = 0,
                schoolId = school.schoolId,
                grade = state.grade!!,
                letter = state.letter.getValidate(),
                academicYearStart = state.academicYearStart!!,
                academicYearEnd = state.academicYearEnd!!,
                teacherId = state.classTeacher?.userId
            )
            executeWithLoadingSync(
                "save",
                { classRepository.addClass(newClass) }) { routerAdmin.navigateBack() }
        }
    }

    fun cancel() {
        routerAdmin.navigateBack()
    }
}