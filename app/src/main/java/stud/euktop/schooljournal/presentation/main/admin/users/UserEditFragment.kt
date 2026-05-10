package stud.euktop.schooljournal.presentation.main.admin.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserRole
import stud.euktop.schooljournal.databinding.DialogUserEditBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.*
import stud.euktop.schooljournal.presentation.main.admin.dialog.role_shool.RoleSchoolEditDialog
import stud.euktop.uikit.components.input.select.ListSafe
import javax.inject.Inject

@AndroidEntryPoint
class UserEditFragment : BaseFragment<
        DialogUserEditBinding,
        UserEditViewModel,
        UserEditState,
        UserEditEvent
        >() {

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        DialogUserEditBinding.inflate(i, c, false)

    override val viewModel: UserEditViewModel by viewModels()

    private val focusTrack = FocusTrack()

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun setupUI() {
        binding.apply {
            inputLastName.setup(focusTrack) { viewModel.updateLastName(it) }
            inputFirstName.setup(focusTrack) { viewModel.updateFirstName(it) }
            inputSurName.setup(focusTrack) { viewModel.updateSurName(it) }
            inputEmail.setup(focusTrack) { viewModel.updateEmail(it) }
            inputPhone.setup(focusTrack) { viewModel.updatePhone(it) }
            inputPassword.setup(focusTrack) { viewModel.updatePassword(it) }

            if (viewModel.canEditFull()) {
                selectStatus.RegisterList(
                    items = ListSafe(
                        values = AccountStatus.entries.toList(),
                        toText = { it?.let { requireContext().getString(it.toMessageId()) } ?: "" },
                        onClick = { status, _ ->
                            viewModel.updateAccountStatus(status ?: AccountStatus.ACTIVE)
                        }
                    )
                ).register(childFragmentManager)
            } else {
                selectStatus.visibility = View.GONE
            }

            val rolesAdapter = RoleSchoolAdapter(
                onDeleteClick = { role ->
                    viewModel.removeRole(role)
                }
            )
            rvRoles.adapter = rolesAdapter

            btnAddRole.setOnClickListener { showRoleSchoolDialog() }

            saveCancel.btnSave.setOnClickListener { viewModel.save() }
            saveCancel.btnCancel.setOnClickListener { navigationManager.navigate(NavCommand.Back) }
        }
    }

    private fun showRoleSchoolDialog(role: Role? = null, school: School? = null) {
        if (childFragmentManager.findFragmentByTag(RoleSchoolEditDialog.TAG) != null) return
        val dialog = RoleSchoolEditDialog.newInstance(role, school, viewModel) { r, s ->
            viewModel.addRole(UserRole(r, s))
        }
        dialog.show(childFragmentManager, RoleSchoolEditDialog.TAG)
    }

    override fun updateState(state: UserEditState) {
        binding.apply {
            inputChecks(
                focusTrack,
                inputLastName to state.lastName,
                inputFirstName to state.firstName,
                inputSurName to state.surName,
                inputEmail to state.email,
                inputPhone to state.phone,
                inputPassword to state.password
            )

            if (viewModel.canEditFull()) {
                selectStatus.state = selectStatus.state.copy(
                    selectText = requireContext().getString(state.accountStatus.toMessageId())
                )
            }
            binding.rvRoles.submitList(state.selectedRoles)
            saveCancel.btnSave.isEnabled = state.isFormValid() && !state.isLoading
        }
    }

    override fun updateEvent(event: UserEditEvent) {
        when (event) {
            UserEditEvent.NavigateBack -> navigationManager.navigate(NavCommand.Back)
        }
    }

    companion object {
        private const val TAG = "UserEditFragment"
    }
}