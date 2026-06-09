package stud.euktop.schooljournal.presentation.main.admin.home

import dagger.hilt.android.lifecycle.HiltViewModel
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AdminHomeViewModel @Inject constructor() : BaseViewModel<AdminHomeState, Unit>() {
    override fun initState() = AdminHomeState()
}

