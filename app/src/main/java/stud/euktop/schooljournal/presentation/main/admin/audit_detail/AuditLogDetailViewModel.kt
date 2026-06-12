package stud.euktop.schooljournal.presentation.main.admin.audit_detail

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.repository.AuditLogRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class AuditLogDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AuditLogRepository,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<AuditLogDetailState, Unit>() {

    private val logId: Int = savedStateHandle["id"] ?: 0

    override fun initState() = AuditLogDetailState()

    init {
        executeCoordinator = coordinatorExec
        loadLog()
    }

    private fun loadLog() {
        executeWithLoadingSync(
            key = "load_log",
            block = { repository.getLogById(logId) }
        ) { log ->
            _state.update { it.copy(log = log) }
        }
    }
}