package stud.euktop.schooljournal.presentation.main.teacher.homework

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.common.Field
import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.homework.HomeworkUpdate
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.domain.repository.HomeworkRepository
import stud.euktop.domain.repository.LessonRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.coordinator.HomeworkCoordinator
import stud.euktop.schooljournal.presentation.common.filter.homework.AppHomeworkFilter
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TeacherHomeworkViewModel @Inject constructor(
    private val coordinator: HomeworkCoordinator,
    private val authRepository: AuthRepository,
    private val lessonRepository: LessonRepository,
    private val homeworkRepository: HomeworkRepository,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<TeacherHomeworkState, TeacherHomeworkEvent>() {

    override fun initState() = TeacherHomeworkState()

    init {
        executeCoordinator = coordinatorExec
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        executeWithResultLoadingSync("load_user", { authRepository.getCurrentUser() }) { _ ->
            loadAvailableLessons()
            loadHomeworkList()
        }
    }

    private fun loadAvailableLessons() {
        executeWithResultLoadingSync("load_lessons", { lessonRepository.getLessons() }) { lessons ->
            _state.update { it.copy(availableLessons = lessons) }
        }
    }

    private fun loadHomeworkList() {
        executeCoordinatorResultLoadingBlockSync(
            "load_homework",
            { coordinator.getHomeworksWithDetails(_state.value.homeworkFilter.toDomain()) }) { list ->
            _state.update { it.copy(homeworkList = list) }
        }
    }

    fun applyFilter(filter: AppHomeworkFilter) {
        _state.update { it.copy(homeworkFilter = filter) }
        loadHomeworkList()
    }

    fun selectLesson(lessonId: Int?) {
        val lesson = _state.value.availableLessons.find { it.lessonId == lessonId }
        _state.update { it.copy(selectedLesson = lesson) }
    }

    fun updateDescription(desc: String) {
        _state.update { it.copy(description = it.description.copy(desc)) }
    }

    fun setEditMode(homeworkId: Int) {
        // загружаем конкретное ДЗ через координатор (или репозиторий)
        executeCoordinatorResultLoadingBlockSync(
            "load_homework_item",
            { coordinator.getHomeworkWithDetails(homeworkId) }) { full ->
            _state.update {
                it.copy(
                    isEditMode = true,
                    editingHomeworkId = homeworkId,
                    selectedLesson = full.lesson,
                    description = it.description.copy(full.description)
                )
            }
        }
    }

    fun save() {
        val state = _state.value
        if (!state.isFormValid()) return
        if (state.isEditMode) {
            val update = HomeworkUpdate(
                homeworkId = state.editingHomeworkId,
                description = Field(state.description.getValidate(), true)
            )
            executeWithResultLoadingSync("save", { homeworkRepository.updateHomework(update) }) {
                _event.tryEmit(TeacherHomeworkEvent.NavigateBack)
            }
        } else {
            val newHomework = Homework(
                lessonId = state.selectedLesson!!.lessonId,
                description = state.description.getValidate(),
                createdAt = Date(),
                medias = emptyList(),
                createdByUserId = 0
            )
            executeWithResultLoadingSync("save", { homeworkRepository.addHomework(newHomework) }) {
                loadHomeworkList()  // обновим список после добавления
                _event.tryEmit(TeacherHomeworkEvent.NavigateBack)
            }
        }
    }

    fun cancel() = _event.tryEmit(TeacherHomeworkEvent.NavigateBack)
}