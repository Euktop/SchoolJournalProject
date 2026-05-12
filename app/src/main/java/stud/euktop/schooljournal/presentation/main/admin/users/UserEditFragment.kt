package stud.euktop.schooljournal.presentation.main.admin.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.schooljournal.databinding.DialogUserEditBinding
import stud.euktop.schooljournal.presentation.common.adapter.ListSelectAdapter
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.binding.bindForm
import stud.euktop.schooljournal.presentation.common.binding.toInit
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.toMessageId
import stud.euktop.schooljournal.presentation.main.admin.dialog.role_shool.RoleSchoolEditDialog
import javax.inject.Inject

@AndroidEntryPoint
class UserEditFragment : BaseFragment<
        DialogUserEditBinding,
        UserEditViewModel,
        UserEditState,
        Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        DialogUserEditBinding.inflate(inflater, container, false)

    override val viewModel: UserEditViewModel by viewModels()
    private val focusTrack = FocusTrack()
    private lateinit var loadingDelegate: LoadingDelegate<UserEditState>

    @Inject
    lateinit var navigationManager: NavigationManager

    private lateinit var rolesAdapter: RoleSchoolAdapter

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        // Bind текстовых полей через bindForm
        bindForm(focusTrack, viewModel) {
            field(binding.inputLastName, { it.lastName }, viewModel::updateLastName)
            field(binding.inputFirstName, { it.firstName }, viewModel::updateFirstName)
            field(binding.inputSurName, { it.surName }, viewModel::updateSurName)
            field(binding.inputEmail, { it.email }, viewModel::updateEmail)
            field(binding.inputPhone, { it.phone }, viewModel::updatePhone)
            field(binding.inputPassword, { it.password }, viewModel::updatePassword)
        }

        // Статус (SchJSelect) – без изменений
        val statusAdapter = ListSelectAdapter<AccountStatus>(
            toText = { status ->
                status?.let { requireContext().getString(status.toMessageId()) } ?: ""
            },
            onItemSelected = { status -> if (status != null) viewModel.updateAccountStatus(status) }
        )
        statusAdapter.submitList(AccountStatus.entries.toList())
        binding.selectStatus.attach(statusAdapter, statusAdapter, childFragmentManager)

        // Адаптер для ролей – с диалогом подтверждения удаления
        rolesAdapter = RoleSchoolAdapter { role ->
            // Показать диалог подтверждения перед удалением
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Удаление роли")
                .setMessage("Вы уверены, что хотите удалить роль ${requireContext().getString(role.role.toMessageId())}?")
                .setPositiveButton("Удалить") { _, _ -> viewModel.removeRole(role) }
                .setNegativeButton("Отмена", null)
                .show()
        }
        binding.rvRoles.adapter = rolesAdapter

        binding.btnAddRole.setOnClickListener { showRoleSchoolDialog() }

        binding.saveCancel.toInit(loadingDelegate, viewModel::save, viewModel::cancel)
    }

    private fun showRoleSchoolDialog() {
        if (childFragmentManager.findFragmentByTag(RoleSchoolEditDialog.TAG) != null) return
        val dialog = RoleSchoolEditDialog.newInstance(
            onError = viewModel,
            onSuccess = { role, school ->
                viewModel.addRole(role, school?.schoolId)
            }
        )
        dialog.show(childFragmentManager, RoleSchoolEditDialog.TAG)
    }

    override fun updateState(state: UserEditState) {
        binding.selectStatus.state = binding.selectStatus.state.copy(
            selectText = requireContext().getString(state.accountStatus.toMessageId())
        )
        rolesAdapter.submitList(state.selectedRoles)
        binding.saveCancel.btnSave.isEnabled = state.isFormValid() && !state.isAnyLoading()
    }

    override fun updateEvent(event: Unit) {}
}