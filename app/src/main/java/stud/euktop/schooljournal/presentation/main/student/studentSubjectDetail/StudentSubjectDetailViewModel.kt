package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class StudentSubjectDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<StudentSubjectDetailState, Unit>() {

    init {
        executeCoordinator = coordinatorExec
        // TODO: Загрузить детали предмета из StudentRepository по subjectId
        //  val subjectId = savedStateHandle.get<Int>(KEY_SUBJECT_ID)
        //  executeWithLoadingSync(...)
    }

    override fun initState() = StudentSubjectDetailState()

    fun onTabSelected(position: Int) {
        _state.update { it.copy(selectedTab = position) }
    }

    fun onGradeClick(item: StudentSubjectGradeItem) {
        // TODO: Открыть детальный просмотр оценки / диалог
    }

    fun onAllGradesClick() {
        // TODO: Перейти к полному списку оценок
    }

    fun onBackClick() {
        // TODO: Навигация назад через Router
    }

    companion object {
        const val KEY_SUBJECT_ID = "subjectId"
    }
}