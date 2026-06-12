package stud.euktop.schooljournal.presentation.main.admin.lessons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import stud.euktop.domain.model.common.Field
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.lesson.LessonUpdate
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.domain.repository.ClassAdminRepository
import stud.euktop.domain.repository.LessonRepository
import stud.euktop.domain.repository.RoomAdminRepository
import stud.euktop.domain.repository.SubjectAdminRepository
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class LessonEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val lessonRepository: LessonRepository,
    private val classRepository: ClassAdminRepository,
    private val subjectRepository: SubjectAdminRepository,
    private val userRepository: UserAdminRepository,
    private val roomRepository: RoomAdminRepository,
    private val routerAdmin: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<LessonEditState, Unit>() {

    private val lessonId: Int = savedStateHandle["lessonId"] ?: 0
    private val isEditMode get() = lessonId != 0

    override fun initState() = LessonEditState(lessonId = lessonId)

    init {
        executeCoordinator = coordinatorExec
        if (isEditMode) loadLesson()
    }

    private fun loadLesson() {
        executeWithResultLoadingSync(
            key = "load",
            block = { lessonRepository.getLesson(lessonId) },
            onSuccess = { full ->
                _state.update {
                    it.copy(
                        selectedClass = full.classInfo,
                        selectedSubject = full.subject,
                        selectedTeacher = UserListItem.createObject(
                            userId = full.teacher.userId,
                            lastName = full.teacher.lastName,
                            firstName = full.teacher.firstName,
                            surName = full.teacher.surName
                        ),
                        date = full.date,
                        topic = full.topic,
                        startTime = full.startTime,
                        endTime = full.endTime,
                        selectedRoom = full.room,
                        locationAddress = full.locationAddress ?: ""
                    )
                }
            }
        )
    }

    suspend fun loadClasses(): List<ClassInfo> =
        classRepository.getClasses().getOrElse { emptyList() }

    suspend fun loadSubjects(): List<Subject> =
        subjectRepository.getSubjects().getOrElse { emptyList() }

    suspend fun loadTeachers(): List<UserListItem> =
        userRepository.getUsers(UserFilter(role = Role.TEACHER)).getOrElse { emptyList() }

    suspend fun loadRooms(): List<Room> = roomRepository.getRooms().getOrElse { emptyList() }

    fun updateClass(classInfo: ClassInfo?) {
        _state.update { it.copy(selectedClass = classInfo) }
    }

    fun updateSubject(subject: Subject?) {
        _state.update { it.copy(selectedSubject = subject) }
    }

    fun updateTeacher(teacher: UserListItem?) {
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
        if (isEditMode) {
            val update = LessonUpdate(
                lessonId = lessonId,
                classId = Field(state.selectedClass?.classId, state.selectedClass != null),
                subjectId = Field(state.selectedSubject?.subjectId, state.selectedSubject != null),
                teacherId = Field(state.selectedTeacher?.userId, state.selectedTeacher != null),
                date = Field(state.date, state.date != null),
                topic = Field(state.topic.takeIf { it.isNotBlank() }, true),
                startTime = Field(state.startTime, true),
                endTime = Field(state.endTime, true),
                roomId = Field(state.selectedRoom?.roomId, state.selectedRoom != null),
                locationAddress = Field(state.locationAddress.takeIf { it.isNotBlank() }, true)
            )
            executeWithResultLoadingSync(
                "save",
                { lessonRepository.updateLesson(update) }) { routerAdmin.toBack() }
        } else {
            val lesson = Lesson(
                classId = state.selectedClass!!.classId,
                subjectId = state.selectedSubject!!.subjectId,
                teacherId = state.selectedTeacher!!.userId,
                date = state.date!!,
                topic = state.topic.takeIf { it.isNotBlank() },
                startTime = state.startTime,
                endTime = state.endTime,
                roomId = state.selectedRoom?.roomId,
                locationAddress = state.locationAddress.takeIf { it.isNotBlank() }
            )
            executeWithResultLoadingSync(
                "save",
                { lessonRepository.addLesson(lesson) }) { routerAdmin.toBack() }
        }
    }

    fun cancel() {
        viewModelScope.launch {
            routerAdmin.toBack()
        }
    }
}