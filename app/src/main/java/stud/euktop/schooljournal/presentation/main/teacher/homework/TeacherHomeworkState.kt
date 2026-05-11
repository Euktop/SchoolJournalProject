package stud.euktop.schooljournal.presentation.main.teacher.homework

import stud.euktop.domain.model.homework.HomeworkFull
import stud.euktop.domain.model.lesson.LessonFull
import stud.euktop.domain.utils.validation.TextThereValidator
import stud.euktop.domain.utils.validation.Validator
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.filter.homework.AppHomeworkFilter

data class TeacherHomeworkState(
    val homeworkList: List<HomeworkFull> = emptyList(),
    val availableLessons: List<LessonFull> = emptyList(),
    val selectedLesson: LessonFull? = null,
    val description: TextThereValidator = TextThereValidator(),
    val isEditMode: Boolean = false,
    val editingHomeworkId: Int = 0,
    val homeworkFilter: AppHomeworkFilter = AppHomeworkFilter(),  // ← здесь
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<TeacherHomeworkState>() {
    fun isFormValid(): Boolean = Validator.isAllValidate(description) && selectedLesson != null
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}