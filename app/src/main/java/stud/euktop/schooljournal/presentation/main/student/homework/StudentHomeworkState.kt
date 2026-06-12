package stud.euktop.schooljournal.presentation.main.student.homework

import stud.euktop.domain.model.homework.HomeworkFull
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.filter.homework.AppHomeworkFilter

data class StudentHomeworkState(
    val homeworkList: List<HomeworkFull> = emptyList(),
    val filter: AppHomeworkFilter = AppHomeworkFilter(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<StudentHomeworkState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): StudentHomeworkState =
        copy(loadingMap = loadingMap)
}