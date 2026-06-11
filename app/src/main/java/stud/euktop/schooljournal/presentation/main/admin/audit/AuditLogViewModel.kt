package stud.euktop.schooljournal.presentation.main.admin.audit

import stud.euktop.schooljournal.presentation.common.base.BaseViewModel

class AuditLogViewModel : BaseViewModel<AuditLogState, Unit>() {
    override fun initState() = AuditLogState()
}
