package stud.euktop.schooljournal.presentation.main.student.home

import dagger.hilt.android.lifecycle.HiltViewModel
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class StudentHomeViewModel @Inject constructor() : BaseViewModel<StudentHomeState, Unit>() {
    override fun initState() = StudentHomeState()
}

