package stud.euktop.schooljournal.presentation.main.admin.audit

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.audit.AuditFilter
import stud.euktop.domain.model.audit.AuditLogListItem
import stud.euktop.domain.repository.AuditLogRepository
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import stud.euktop.schooljournal.presentation.common.paging.AuditLogPagingSource
import javax.inject.Inject

@HiltViewModel
class AuditLogViewModel @Inject constructor(
    private val repository: AuditLogRepository,
    private val routerAdmin: RouterAdmin,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<AuditLogState, Unit>() {
    init {
        executeCoordinator = coordinatorExec
    }

    override fun initState() = AuditLogState()

    private val pagingSourceFactory = {
        AuditLogPagingSource(repository, _state.value.filter)
    }

    private val filterFlow = MutableStateFlow(_state.value.filter)

    val pagingFlow = filterFlow.flatMapLatest { filter ->
        Pager(PagingConfig(pageSize = 20)) {
            AuditLogPagingSource(repository, filter)
        }.flow.cachedIn(viewModelScope)
    }

    fun applyFilter(filter: AuditFilter) {
        _state.update { it.copy(filter = filter) }
        filterFlow.value = filter
    }

    fun loadStatistics() {
        executeWithResultLoadingSync(
            key = "stats",
            block = { repository.getStatistics() },
            onSuccess = { stats -> _state.update { it.copy(statistics = stats) } }
        )
    }

    fun onLogClick(item: AuditLogListItem) {
        routerAdmin.toAuditLogDetail(item.id)
    }

    init {
        loadStatistics()
    }
}