package stud.euktop.schooljournal.presentation.main.admin.audit_detail

import dagger.hilt.android.lifecycle.HiltViewModel
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class AuditLogDetailViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec
) : BaseViewModel<AuditLogDetailState, Unit>() {
    init {
        executeCoordinator = coordinatorExec
    }

    override fun initState() = AuditLogDetailState()
}