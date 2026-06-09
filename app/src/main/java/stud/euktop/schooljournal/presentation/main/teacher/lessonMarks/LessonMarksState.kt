package stud.euktop.schooljournal.presentation.main.teacher.lessonMarks

import stud.euktop.domain.model.attendance.StudentMarkItem
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class LessonMarksState(
    val marks: List<StudentMarkItem> = emptyList(),
    val classAndSubject: String = "",
    val selectedChipIndex: Int = 0,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<LessonMarksState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): LessonMarksState =
        copy(loadingMap = loadingMap)
}