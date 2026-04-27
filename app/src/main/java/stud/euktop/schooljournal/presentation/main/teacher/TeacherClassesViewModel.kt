package stud.euktop.schooljournal.presentation.main.teacher

import dagger.hilt.android.lifecycle.HiltViewModel
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class TeacherClassesViewModel @Inject constructor() : BaseViewModel<TeacherClassesState, Unit>() {
    override fun initState() = TeacherClassesState()
}