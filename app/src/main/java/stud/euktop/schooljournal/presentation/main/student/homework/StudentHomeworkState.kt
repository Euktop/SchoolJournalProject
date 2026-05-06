// presentation/main/student/homework/StudentHomeworkState.kt
package stud.euktop.schooljournal.presentation.main.student.homework

import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.homework.HomeworkFilter
import stud.euktop.domain.model.homework.HomeworkFilter2
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class StudentHomeworkState(
    override val isLoading: Boolean = false,
    val homeworkList: List<Homework> = emptyList(),
    val filter: HomeworkFilter2 = HomeworkFilter2()
) : BaseState<StudentHomeworkState>() {
    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}