package stud.euktop.schooljournal.presentation.main.student.schedule

import stud.euktop.domain.model.schedule.StudentScheduleItem
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.filter.lesson.AppLessonFilter

data class StudentScheduleState(
    val schedule: List<StudentScheduleItem> = emptyList(),
    val filter: AppLessonFilter = AppLessonFilter(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<StudentScheduleState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}