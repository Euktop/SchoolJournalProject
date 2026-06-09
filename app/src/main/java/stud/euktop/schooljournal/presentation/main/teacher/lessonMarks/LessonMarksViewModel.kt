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
        loadClassAndSubject()
    }

    fun loadMarks() {
        executeWithLoadingSync(
            key = "load_marks",
            block = { repository.getMarks(lessonId) },
            onSuccess = { marks -> _state.update { it.copy(marks = marks) } }
        )
    }

    private fun loadClassAndSubject() {
        // TODO: Реализовать загрузку названия класса и предмета через LessonRepository.getLesson(lessonId)
        // Пока захардкодим для мока, чтобы UI сразу выглядел красиво
        _state.update { it.copy(classAndSubject = "5А – Математика") }
    }

    fun selectChip(index: Int) {
        _state.update { it.copy(selectedChipIndex = index) }
        // TODO: В будущем фильтровать список marks в зависимости от выбранного чипа
    }

    fun saveGrade(studentId: Int, absenceTypes: AbsenceTypes, comment: String?) {
        executeWithLoadingSync(
            key = "save_grade",
            block = { gradeRepository.addGrade(lessonId, studentId, absenceTypes, comment) },
            onSuccess = { loadMarks() }
        )
    }
}