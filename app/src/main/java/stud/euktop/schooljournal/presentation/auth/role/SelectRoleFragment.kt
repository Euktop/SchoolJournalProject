package stud.euktop.schooljournal.presentation.auth.role

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentSelectRoleBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.domain.utils.loger.logger

@AndroidEntryPoint
class SelectRoleFragment : BaseFragment<
        FragmentSelectRoleBinding,
        SelectRoleViewModel,
        SelectRoleState,
        Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSelectRoleBinding.inflate(inflater, container, false)

    override val viewModel: SelectRoleViewModel by viewModels()

    private lateinit var adapter: RoleAdapter

    override fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.onBack()
        }

        adapter = RoleAdapter { role ->
            viewModel.selectRole(role)
        }
        binding.rvRoles.adapter = adapter

        binding.btnContinue.setOnClickListener {
            viewModel.onContinueClick()
        }
    }

     override fun updateState(state: SelectRoleState) {
         logger?.d(this::class.java.simpleName, "updateState", "roles count: ${state.roles.size}, button active: ${state.isButtonActive()}")
         adapter.submitList(state.roles)
         binding.btnContinue.isEnabled = state.isButtonActive()
     }

    override fun updateEvent(event: Unit) {}
}