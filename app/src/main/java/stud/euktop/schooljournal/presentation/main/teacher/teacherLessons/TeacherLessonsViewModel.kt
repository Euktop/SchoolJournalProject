package stud.euktop.schooljournal.presentation.main.teacher.teacherLessons

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.coordinator.TeacherLessonsCoordinator
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

/**
 * ViewModel для экрана уроков учителя.
 *
 * Назначение: загружает уроки по переданным classId и subjectId.
 *
 * Функционал:
 * - Извлечение аргументов (classId, subjectId) из SavedStateHandle
 * - State: lessons (List<TeacherLessonItem>), isLoading
 * - loadLessons() – вызов TeacherLessonsRepository.getLessons()
 */
@HiltViewModel
class TeacherLessonsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val coordinator: TeacherLessonsCoordinator,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<TeacherLessonsState, Unit>() {
    companion object {
        const val CLASS_ID = "classId"
        const val SUBJECT_ID = "subjectId"
    }

    private val classId = savedStateHandle.get<Int>(CLASS_ID) ?: 0
    private val subjectId = savedStateHandle.get<Int>(SUBJECT_ID) ?: 0

    override fun initState() = TeacherLessonsState()

    init {
        executeCoordinator = coordinatorExec
        loadLessons()
    }

    fun loadLessons() {
        executeLoadingBlockSync(
            key = "load_lessons",
            block = { coordinator.getLessons(classId, subjectId) },
            onSuccess = { lessons -> _state.update { it.copy(lessons = lessons) } }
        )
    }
}