package stud.euktop.schooljournal.presentation.common.filter.subject

import dagger.hilt.android.lifecycle.HiltViewModel
import stud.euktop.schooljournal.presentation.common.base.BaseFilterViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class SubjectFilterViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec
) : BaseFilterViewModel(coordinatorExec)