package stud.euktop.schooljournal.presentation.main.teacher.homework

import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.utils.validation.TextThereValidator
import stud.euktop.domain.utils.validation.Validator
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class TeacherHomeworkState(
    override val isLoading: Boolean = false,
    val homeworkList: List<Homework> = emptyList(),
    val availableLessons: List<Lesson> = emptyList(),
    val selectedLesson: Lesson? = null,
    val description: TextThereValidator = TextThereValidator(),
    val attachedFiles: String = "",
    val isEditMode: Boolean = false,
    val editingHomeworkId: Int = 0
) : BaseState<TeacherHomeworkState>() {

    fun isFormValid(): Boolean = Validator.isAllValidate(description) && selectedLesson != null

    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}