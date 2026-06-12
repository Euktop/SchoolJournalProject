package stud.euktop.schooljournal.presentation.common.filter.audit

import dagger.hilt.android.lifecycle.HiltViewModel
import stud.euktop.domain.model.audit.ActionType
import stud.euktop.schooljournal.presentation.common.base.BaseFilterViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class AuditLogFilterViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec
) : BaseFilterViewModel(coordinatorExec) {


    fun loadActionTypes(): List<ActionType> {
        return ActionType.entries
    }
}