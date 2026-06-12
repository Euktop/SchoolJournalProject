package stud.euktop.schooljournal.presentation.main.teacher.teacherLessons

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.coordinator.TeacherLessonsCoordinator
import stud.euktop.schooljournal.presentation.common.filter.lesson.AppLessonFilter
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterTeacher
import javax.inject.Inject

@HiltViewModel
class TeacherLessonsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val coordinator: TeacherLessonsCoordinator,
    private val router: RouterTeacher,
    coordinatorExec: CoordinatorExec,
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

    fun applyFilter(filter: AppLessonFilter) {
        _state.update { it.copy(filter = filter, lessons = emptyList()) }
        loadLessons()
    }

    private fun loadLessons() {
        val filter = _state.value.filter
        executeLoadingBlockSync(
            key = "load_lessons",
            block = { coordinator.getLessons(classId, subjectId, filter.dateFrom, filter.dateTo) },
            onSuccess = { lessons -> _state.update { it.copy(lessons = lessons) } }
        )
    }

    fun onLessonClick(lesson: TeacherLessonItem) {
        router.toTeacherLessonMarks(lesson.lessonId)
    }

}