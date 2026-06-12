package stud.euktop.schooljournal.presentation.main.teacher.dashboard

import dagger.hilt.android.lifecycle.HiltViewModel
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class TeacherDashboardViewModel @Inject constructor() : BaseViewModel<TeacherDashboardState, Unit>() {
    override fun initState() = TeacherDashboardState()
}

