package stud.euktop.schooljournal.presentation.main.admin.audit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentAdminEntityBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider

@AndroidEntryPoint
class AuditLogFragment :
    BaseFragment<FragmentAdminEntityBinding, AuditLogViewModel, AuditLogState, Unit>(),
    ToolbarConfigProvider {

    override val viewModel: AuditLogViewModel by viewModels()
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdminEntityBinding.inflate(inflater, container, false)

    override fun setupUI() {
    }

    override fun updateState(state: AuditLogState) {}
    override fun updateEvent(event: Unit) {}

    override fun getToolbarConfig() = ToolbarConfig(titleRes = R.string.audit_log)
}