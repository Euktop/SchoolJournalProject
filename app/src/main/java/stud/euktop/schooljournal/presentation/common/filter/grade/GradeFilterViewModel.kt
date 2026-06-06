package stud.euktop.schooljournal.presentation.common.filter.grade

import dagger.hilt.android.lifecycle.HiltViewModel
import stud.euktop.schooljournal.presentation.common.base.BaseFilterViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class GradeFilterViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec
) : BaseFilterViewModel(coordinatorExec)