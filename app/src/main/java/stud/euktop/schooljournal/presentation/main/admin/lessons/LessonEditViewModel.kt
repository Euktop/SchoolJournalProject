package stud.euktop.schooljournal.presentation.main.admin.lessons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.repository.*
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class LessonEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager,
    private val classAdminRepository: ClassAdminRepository,
    private val subjectAdminRepository: SubjectAdminRepository,
    private val userAdminRepository: UserAdminRepository,
    private val roomAdminRepository: RoomAdminRepository,
    private val lessonRepository: LessonRepository
) : BaseViewModel<LessonEditState, LessonEditEvent>() {

    companion object {
        const val KEY_LESSON_ID = "lessonId"
    }

    private val lessonId: Int = savedStateHandle[KEY_LESSON_ID] ?: 0

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        loadInitialData()
        if (lessonId != 0) loadLesson()
    }

    override fun initState() = LessonEditState(lessonId = lessonId)

    private fun loadInitialData() {
        viewModelScope.launch {
            loadClasses()
            loadSubjects()
            loadTeachers()
            loadRooms()
        }
    }

    private fun loadLesson() {
        executeWithCoordinatorAndLoadingSync(
            block = { lessonRepository.getLesson(lessonId) },
            onSuccess = { lesson ->
                _state.update {
                    it.copy(
                        lessonId = lesson.lessonId,
                        selectedClass = lesson.classInfo,
                        selectedSubject = lesson.subject,
                        selectedTeacher = lesson.teacher,
                        date = lesson.date,
                        topic = lesson.topic ?: "",
                        startTime = lesson.startTime,
                        endTime = lesson.endTime,
                        selectedRoom = lesson.room,
                        locationAddress = lesson.locationAddress ?: ""
                    )
                }
            }
        )
    }

    suspend fun loadClasses(): List<ClassInfo> {
        val result = classAdminRepository.getClasses()
        return result.getOrElse { emptyList() }
    }

    suspend fun loadSubjects(): List<Subject> {
        val result = subjectAdminRepository.getSubjects()
        return result.getOrElse { emptyList() }
    }

    suspend fun loadTeachers(): List<UserInfo> {
        val result = userAdminRepository.getTeachersByRole(Role.TEACHER)
        return result.getOrElse { emptyList() }
    }

    suspend fun loadRooms(): List<Room> {
        val result = roomAdminRepository.getRooms()
        return result.getOrElse { emptyList() }
    }

    // Обновление полей
    fun updateClass(classInfo: ClassInfo?) {
        _state.update { it.copy(selectedClass = classInfo) }
    }

    fun updateSubject(subject: Subject?) {
        _state.update { it.copy(selectedSubject = subject) }
    }

    fun updateTeacher(teacher: UserInfo?) {
        _state.update { it.copy(selectedTeacher = teacher) }
    }

    fun updateDate(date: Date?) {
        _state.update { it.copy(date = date) }
    }

    fun updateTopic(topic: String) {
        _state.update { it.copy(topic = topic) }
    }

    fun updateStartTime(time: String) {
        _state.update { it.copy(startTime = time) }
    }

    fun updateEndTime(time: String) {
        _state.update { it.copy(endTime = time) }
    }

    fun updateRoom(room: Room?) {
        _state.update { it.copy(selectedRoom = room) }
    }

    fun updateLocationAddress(address: String) {
        _state.update { it.copy(locationAddress = address) }
    }

    fun save() {
        val state = _state.value
        if (!state.isFormValid()) return

        val lesson = Lesson(
            lessonId = state.lessonId,
            classInfo = state.selectedClass!!,
            subject = state.selectedSubject!!,
            teacher = state.selectedTeacher!!,
            date = state.date!!,
            topic = state.topic.takeIf { it.isNotBlank() },
            startTime = state.startTime,
            endTime = state.endTime,
            room = state.selectedRoom,
            locationAddress = state.locationAddress.takeIf { it.isNotBlank() }
        )

        executeWithCoordinatorAndLoadingSync(
            block = {
                if (state.isEditMode()) lessonRepository.updateLesson(lesson)
                else lessonRepository.addLesson(lesson)
            },
            onSuccess = { _event.emit(LessonEditEvent.NavigateBack) }
        )
    }
}