package stud.euktop.schooljournal.presentation.main.student.schedule

import stud.euktop.domain.model.schedule.StudentScheduleItem
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class StudentScheduleState(
    val schedule: List<StudentScheduleItem> = emptyList(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<StudentScheduleState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}