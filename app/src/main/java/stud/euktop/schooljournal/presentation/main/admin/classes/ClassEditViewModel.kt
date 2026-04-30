package stud.euktop.schooljournal.presentation.main.admin.classes

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.ClassInfo
import stud.euktop.domain.model.Role
import stud.euktop.domain.repository.AdminRepository
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
    private val adminRepository: AdminRepository
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

    /**
     * Загружает списки школ и учителей параллельно с единой индикацией загрузки.
     */
    private fun loadInitialData() {
        executeLoadingBlockSync {
            val schoolsDeferred = async { adminRepository.getSchools() }
            val teachersDeferred = async { adminRepository.getTeachersByRole(Role.TEACHER) }

            schoolsDeferred.await().onSuccess { schools ->
                _state.update { it.copy(availableSchools = schools) }
            }.onFailure {
                _messageEvent.tryEmit(
                    MessageEvent.Message(
                        MessageParam(R.string.error_load_schools)
                    )
                )
            }
            teachersDeferred.await().onSuccess { teachers ->
                _state.update { it.copy(availableTeachers = teachers) }
            }.onFailure {
                _messageEvent.tryEmit(
                    MessageEvent.Message(
                        MessageParam(R.string.error_load_teachers)
                    )
                )

            }
        }
    }

    /**
     * Загружает данные редактируемого класса.
     */
    private fun loadClass() {
        executeWithCoordinatorAndLoadingSync(
            block = { adminRepository.getClass(classId) },
            onSuccess = { classInfo ->
                _state.update {
                    it.copy(
                        classId = classInfo.classId,
                        schoolId = classInfo.schoolId,
                        grade = classInfo.grade,
                        letter = it.letter.copy(classInfo.letter),
                        academicYearStart = classInfo.academicYearStart,
                        academicYearEnd = classInfo.academicYearEnd,
                        classTeacherId = classInfo.teacherId,
                        selectedSchoolName = classInfo.schoolName ?: "",
                        selectedTeacherName = classInfo.teacherName ?: ""
                    )
                }
            })
    }

    // Обновление полей
    fun updateSchool(schoolId: Int, schoolName: String) {
        _state.update {
            it.copy(
                schoolId = schoolId, selectedSchoolName = schoolName
            )
        }
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

    fun updateClassTeacher(teacherId: Int?, teacherName: String) {
        _state.update {
            it.copy(
                classTeacherId = teacherId, selectedTeacherName = teacherName
            )
        }
    }

    fun save() {
        val state = _state.value
        if (!state.isFormValid()) return

        val classInfo = ClassInfo(
            classId = state.classId,
            schoolId = state.schoolId!!,
            schoolName = state.selectedSchoolName,
            grade = state.grade!!,
            letter = state.letter.getValidate(),
            academicYearStart = state.academicYearStart!!,
            academicYearEnd = state.academicYearEnd!!,
            teacherId = state.classTeacherId,
            teacherName = state.selectedTeacherName
        )

        executeWithCoordinatorAndLoadingSync(block = {
            if (state.isEditMode()) {
                adminRepository.updateClass(classInfo)
            } else {
                adminRepository.addClass(classInfo)
            }
        }, onSuccess = { _event.emit(ClassEditEvent.NavigateBack) })
    }
}