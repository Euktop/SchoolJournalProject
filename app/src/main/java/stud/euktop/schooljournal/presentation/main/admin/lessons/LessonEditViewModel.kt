package stud.euktop.schooljournal.presentation.main.admin.lessons

import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel

data class LessonEditState(override val loadingMap: Map<String, Boolean> = emptyMap()) :
    BaseState<LessonEditState>() {
    override fun updateLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}

class LessonEditViewModel : BaseViewModel<LessonEditState, Unit>() {
    override fun initState() = LessonEditState()

}