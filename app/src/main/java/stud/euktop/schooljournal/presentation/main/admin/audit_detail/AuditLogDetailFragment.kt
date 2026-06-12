package stud.euktop.schooljournal.presentation.main.admin.audit_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentAuditLogDetailBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment

@AndroidEntryPoint
class AuditLogDetailFragment :
    BaseFragment<FragmentAuditLogDetailBinding, AuditLogDetailViewModel, AuditLogDetailState, Unit>() {
    override val viewModel: AuditLogDetailViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentAuditLogDetailBinding.inflate(inflater, container, false)

    override fun setupUI() {
    }

    override fun updateState(state: AuditLogDetailState) {
    }
}