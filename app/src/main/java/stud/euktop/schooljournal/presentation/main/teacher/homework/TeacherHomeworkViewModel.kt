package stud.euktop.schooljournal.presentation.main.teacher.homework

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.domain.repository.HomeworkRepository
import stud.euktop.domain.repository.TeacherRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TeacherHomeworkViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager,
    private val authRepository: AuthRepository,
    private val teacherRepository: TeacherRepository,
    private val homeworkRepository: HomeworkRepository
) : BaseViewModel<TeacherHomeworkState, TeacherHomeworkEvent>() {

    private var currentUserId: Int = 0

    override fun initState() = TeacherHomeworkState()

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        executeWithCoordinatorAndLoadingSync(
            block = { authRepository.getCurrentUser() },
            onSuccess = { profile ->
                currentUserId = profile.userId
                loadAvailableLessons()
                loadHomeworkList()
            }
        )
    }

    private fun loadAvailableLessons() {
        executeWithCoordinatorAndLoadingSync(
            block = { teacherRepository.getTeacherLessons() },
            onSuccess = { lessons ->
                _state.update { it.copy(availableLessons = lessons) }
            }
        )
    }

    private fun loadHomeworkList() {
        executeWithCoordinatorAndLoadingSync(
            block = { homeworkRepository.getHomeworkByTeacher(currentUserId) },
            onSuccess = { list ->
                _state.update { it.copy(homeworkList = list) }
            }
        )
    }

    fun addHomework(description: String, attachedFiles: String, lessonId: Int) {
        val selectedLesson = _state.value.availableLessons.find { it.lessonId == lessonId }
        if (selectedLesson == null) return
        if (!_state.value.isFormValid()) return

        val newHomework = Homework(
            homeworkId = 0,
            lesson = selectedLesson,
            description = description,
            attachedFiles = attachedFiles.takeIf { it.isNotBlank() },
            createdAt = Date(),
            createdByUser = UserInfo(
                userId = currentUserId,
                lastName = "",
                firstName = "",
                surName = null,
                email = "",
                phone = null,
                roles = emptyList(),
                accountStatus = AccountStatus.ACTIVE
            )
        )
        executeWithCoordinatorAndLoadingSync(
            block = { homeworkRepository.addHomework(newHomework) },
            onSuccess = {
                loadHomeworkList()
                _event.tryEmit(TeacherHomeworkEvent.NavigateBack)
            }
        )
    }

    fun updateHomework(homeworkId: Int, description: String, attachedFiles: String, lessonId: Int) {
        val existingHomework =
            _state.value.homeworkList.find { it.homeworkId == homeworkId } ?: return
        val selectedLesson =
            _state.value.availableLessons.find { it.lessonId == lessonId } ?: return
        val updatedHomework = existingHomework.copy(
            lesson = selectedLesson,
            description = description,
            attachedFiles = attachedFiles.takeIf { it.isNotBlank() }
        )
        executeWithCoordinatorAndLoadingSync(
            block = { homeworkRepository.updateHomework(updatedHomework) },
            onSuccess = {
                loadHomeworkList()
                _event.tryEmit(TeacherHomeworkEvent.NavigateBack)
            }
        )
    }

    fun deleteHomework(homeworkId: Int) {
        executeWithCoordinatorAndLoadingSync(
            block = { homeworkRepository.deleteHomework(homeworkId) },
            onSuccess = { loadHomeworkList() }
        )
    }

    fun selectLesson(lessonId: Int) {
        val lesson = _state.value.availableLessons.find { it.lessonId == lessonId }
        _state.update { it.copy(selectedLesson = lesson) }
    }

    fun updateDescription(description: String) {
        _state.update { it.copy(description = it.description.copy(description)) }
    }

    fun updateAttachedFiles(files: String) {
        _state.update { it.copy(attachedFiles = files) }
    }

    fun setEditMode(homeworkId: Int) {
        val homework = _state.value.homeworkList.find { it.homeworkId == homeworkId }
        if (homework != null) {
            _state.update {
                it.copy(
                    isEditMode = true,
                    editingHomeworkId = homeworkId,
                    selectedLesson = homework.lesson,
                    description = it.description.copy(homework.description),
                    attachedFiles = homework.attachedFiles ?: ""
                )
            }
        }
    }

    fun resetForm() {
        _state.update {
            it.copy(
                isEditMode = false,
                editingHomeworkId = 0,
                selectedLesson = null,
                description = it.description.copy(""),
                attachedFiles = ""
            )
        }
    }
}