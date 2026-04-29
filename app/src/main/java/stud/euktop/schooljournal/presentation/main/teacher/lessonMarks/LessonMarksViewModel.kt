package stud.euktop.schooljournal.presentation.main.teacher.lessonMarks

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.LessonMarksRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject
/**
 * ViewModel для экрана оценок за урок.
 *
 * Назначение: загружает список учеников с их оценками/пропусками.
 *
 * Функционал:
 * - Извлечение lessonId из SavedStateHandle
 * - State: marks (List<StudentMarkItem>), isLoading
 * - loadMarks() – вызов LessonMarksRepository.getMarks()
 */
@HiltViewModel
class LessonMarksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager,
    private val repository: LessonMarksRepository
) : BaseViewModel<LessonMarksState, Unit>() {
    companion object {
        const val LESSON_ID_KEY = "lessonId"
    }

    private val lessonId = savedStateHandle.get<Int>(LESSON_ID_KEY) ?: 0

    override fun initState() = LessonMarksState()

    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
        loadMarks()
    }

    fun loadMarks() {
        executeWithCoordinatorAndLoadingSync(
            block = { repository.getMarks(lessonId) },
            onSuccess = { marks -> _state.update { it.copy(marks = marks) } }
        )
    }
}