package stud.euktop.schooljournal.presentation.main.admin.classes

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.repository.*
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.message.MessageEvent
import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

@HiltViewModel
class ClassEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager,
    private val schoolAdminRepository: SchoolAdminRepository,
    private val userAdminRepository: UserAdminRepository,
    private val classAdminRepository: ClassAdminRepository
) : BaseViewModel<ClassEditState, ClassEditEvent>() {

    companion object {
        const val KEY_CLASS_ID = "classId"
    }

    private val classId: Int = savedStateHandle[KEY_CLASS_ID] ?: 0

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        loadInitialData()
        if (classId != 0) loadClass()
    }

    override fun initState() = ClassEditState(classId = classId)

    private fun loadInitialData() {
        executeLoadingBlockSync {
            val schoolsDeferred = async { schoolAdminRepository.getSchools() }
            val teachersDeferred = async { userAdminRepository.getTeachersByRole(Role.TEACHER) }

            schoolsDeferred.await().onSuccess { schools ->
                _state.update { it.copy(availableSchools = schools) }
            }.onFailure {
                _messageEvent.tryEmit(MessageEvent.Message(MessageParam(R.string.error_load_schools)))
            }
            teachersDeferred.await().onSuccess { teachers ->
                _state.update { it.copy(availableTeachers = teachers) }
            }.onFailure {
                _messageEvent.tryEmit(MessageEvent.Message(MessageParam(R.string.error_load_teachers)))
            }
        }
    }

    private fun loadClass() {
        executeWithCoordinatorAndLoadingSync(
            block = { classAdminRepository.getClass(classId) },
            onSuccess = { classInfo ->
                val school = classInfo.school
                val teacher = classInfo.teacher
                _state.update {
                    it.copy(
                        classId = classInfo.classId,
                        grade = classInfo.grade,
                        letter = it.letter.copy(classInfo.letter),
                        academicYearStart = classInfo.academicYearStart,
                        academicYearEnd = classInfo.academicYearEnd,
                        selectedSchool = school,
                        selectedTeacher = teacher
                    )
                }
            })
    }

    fun updateSchool(school: School?) {
        _state.update { it.copy(selectedSchool = school) }
    }

    fun updateClassTeacher(teacher: UserInfo?) {
        _state.update { it.copy(selectedTeacher = teacher) }
    }

    fun loadSchools(query: SchoolFilter = SchoolFilter()) {
        executeWithCoordinatorAndLoadingSync(
            block = { schoolAdminRepository.getSchools(query) },
            onSuccess = { schools ->
                _state.update { it.copy(availableSchools = schools) }
            })
    }

    fun updateGrade(grade: Int?) {
        _state.update { it.copy(grade = grade) }
    }

    fun updateLetter(letter: String) {
        _state.update { it.copy(letter = it.letter.copy(letter)) }
    }

    fun updateAcademicYearStart(year: Int?) {
        _state.update { it.copy(academicYearStart = year) }
    }

    fun updateAcademicYearEnd(year: Int?) {
        _state.update { it.copy(academicYearEnd = year) }
    }

    fun save() {
        val state = _state.value
        if (!state.isFormValid()) return

        val school = state.selectedSchool ?: return
        val classInfo = ClassInfo(
            classId = state.classId,
            school = school,
            grade = state.grade!!,
            letter = state.letter.getValidate(),
            academicYearStart = state.academicYearStart!!,
            academicYearEnd = state.academicYearEnd!!,
            teacher = state.selectedTeacher
        )
        executeWithCoordinatorAndLoadingSync(block = {
            if (state.isEditMode()) classAdminRepository.updateClass(classInfo)
            else classAdminRepository.addClass(classInfo)
        }, onSuccess = { _event.emit(ClassEditEvent.NavigateBack) })
    }
}