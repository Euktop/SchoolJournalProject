package stud.euktop.schooljournal.presentation.main.admin.audit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import stud.euktop.domain.utils.loger.logger
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentAdminEntityBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.filter.audit.AuditLogFilterDialog
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider

@AndroidEntryPoint
class AuditLogFragment :
    BaseFragment<FragmentAdminEntityBinding, AuditLogViewModel, AuditLogState, Unit>(),
    ToolbarConfigProvider {

    override val viewModel: AuditLogViewModel by viewModels()
    private lateinit var adapter: AuditLogAdapter

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdminEntityBinding.inflate(inflater, container, false)

    override fun setupUI() {
        adapter = AuditLogAdapter { viewModel.onLogClick(it) }
        binding.rvEntity.adapter = adapter
        binding.rvEntity.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewModel.pagingFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                binding.rvEntity.update()
            }
        }
    }

    private fun showFilterDialog() {
        if (childFragmentManager.findFragmentByTag("audit_filter") != null) return
        val dialog = AuditLogFilterDialog(
            initialFilter = viewModel.state.value.filter,
            onFilterApplied = { viewModel.applyFilter(it) },
            onError = viewModel.onError
        )
        try {
            logger?.d(this::class.java.simpleName, "showFilterDialog", "showing audit_filter")
        } catch (_: Throwable) {
        }
        dialog.show(childFragmentManager, "audit_filter")
    }

     override fun updateState(state: AuditLogState) {
         logger?.d(this::class.java.simpleName, "updateState", "filter applied")
         // статистика может быть отображена в тулбаре или отдельном layout
         // для простоты оставим пустым
     }

    override fun updateEvent(event: Unit) {}

    override fun getToolbarConfig() = ToolbarConfig(
        titleRes = R.string.audit_log,
        menuRes = R.menu.menu_home_filter,
        onMenuItemClick = {
            when (it.itemId) {
                R.id.action_filter -> showFilterDialog()
            }
        }
    )
}