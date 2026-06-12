package stud.euktop.schooljournal.presentation.main.teacher.lessonMarks

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.repository.GradeRepository
import stud.euktop.domain.repository.LessonMarksRepository
import stud.euktop.domain.repository.LessonRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class LessonMarksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coordinatorExec: CoordinatorExec,
    private val repository: LessonMarksRepository,
    private val gradeRepository: GradeRepository,
    private val lessonRepository: LessonRepository
) : BaseViewModel<LessonMarksState, Unit>() {

    companion object {
        const val LESSON_ID_KEY = "lessonId"
    }

    private val lessonId = savedStateHandle.get<Int>(LESSON_ID_KEY) ?: 0

    override fun initState() = LessonMarksState()

    init {
        executeCoordinator = coordinatorExec
        loadMarks()
        loadLessonInfo()
    }

    private fun loadLessonInfo() {
        executeWithResultLoadingSync(
            key = "lesson_info",
            block = { lessonRepository.getLesson(lessonId) }
        ) { lesson ->
            val info =
                "${lesson.classInfo.grade}${lesson.classInfo.letter} – ${lesson.subject.name}"
            _state.update { it.copy(classAndSubject = info) }
        }
    }

    init {
        executeCoordinator = coordinatorExec
        loadMarks()
        loadLessonInfo()  // вместо loadClassAndSubject()
    }

    fun loadMarks() {
        executeWithResultLoadingSync(
            key = "load_marks",
            block = { repository.getMarks(lessonId) },
            onSuccess = { marks -> _state.update { it.copy(marks = marks) } }
        )
    }

    fun selectChip(index: Int) {
        _state.update { it.copy(selectedChipIndex = index) }
    }

    fun saveGrade(studentId: Int, absenceTypes: AbsenceTypes?, comment: String?) {
        executeWithResultLoadingSync(
            key = "save_grade",
            block = { gradeRepository.setGrade(lessonId, studentId, absenceTypes, comment) },
            onSuccess = { loadMarks() }
        )
    }
}