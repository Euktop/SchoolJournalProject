package stud.euktop.schooljournal.presentation.main.teacher.home

import dagger.hilt.android.lifecycle.HiltViewModel
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class TeacherHomeViewModel @Inject constructor() : BaseViewModel<TeacherHomeState, Unit>() {
    override fun initState() = TeacherHomeState()
}
