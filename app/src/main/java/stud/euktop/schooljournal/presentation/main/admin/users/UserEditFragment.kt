package stud.euktop.schooljournal.presentation.main.admin.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.auth.Role
import stud.euktop.domain.model.school.School
import stud.euktop.schooljournal.databinding.DialogUserEditBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.*
import stud.euktop.uikit.components.input.select.ListSafe
import stud.euktop.uikit.components.input.select.searchable.SchJSearchableSelect
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

    private lateinit var schoolRegister: SchJSearchableSelect.RegisterList<School, Any>

    override fun setupUI() {
        binding.apply {
            // Поля ввода
            inputLastName.setup(focusTrack) { viewModel.updateLastName(it) }
            inputFirstName.setup(focusTrack) { viewModel.updateFirstName(it) }
            inputSurName.setup(focusTrack) { viewModel.updateSurName(it) }
            inputEmail.setup(focusTrack) { viewModel.updateEmail(it) }
            inputPhone.setup(focusTrack) { viewModel.updatePhone(it) }
            inputPassword.setup(focusTrack) { viewModel.updatePassword(it) }

            // Статус
            selectStatus.RegisterList(
                items = ListSafe(
                    values = AccountStatus.entries.toList(),
                    toText = { requireContext().getString(it.toMessageId()) },
                    onClick = { status, _ -> viewModel.updateAccountStatus(status) }
                )
            ).register(childFragmentManager)

            // Роль
            selectRole.RegisterList(
                items = ListSafe(
                    values = Role.entries.toList(),
                    toText = { requireContext().getString(it.toMessageId()) },
                    onClick = { role, _ ->
                        viewModel.updateSelectedRole(role)
                        val requiresSchool = role != Role.ADMIN
                        schoolLayout.visibility = if (requiresSchool) View.VISIBLE else View.GONE
                    }
                )
            ).register(childFragmentManager)

            // Поисковый выбор школы
            val schoolListSafe = ListSafe<School>(
                toText = { it.name },
                onClick = { school, _ -> viewModel.updateSchool(school) }
            )
            schoolRegister = selectSchool.RegisterList(
                items = schoolListSafe,
                categories = ListSafe(),
                onSearchQueryChanged = { query -> viewModel.loadSchools(query) }
            )
            schoolRegister.register(childFragmentManager)

            btnSave.setOnClickListener { viewModel.save() }
            btnCancel.setOnClickListener { navigationManager.navigate(NavCommand.Back) }
        }
    }

    override fun updateState(state: UserEditState) {
        binding.apply {
            // Валидация
            inputChecks(
                focusTrack,
                inputLastName to state.lastName,
                inputFirstName to state.firstName,
                inputSurName to state.surName,
                inputEmail to state.email,
                inputPhone to state.phone
            )

            // Пароль
            val passwordErrorVisible = if (state.isEditMode()) {
                focusTrack.isTouched(inputPassword) &&
                        !state.password.value.isNullOrBlank() &&
                        !state.password.validate()
            } else {
                focusTrack.isTouched(inputPassword) && !state.password.validate()
            }
            inputPassword.state = inputPassword.state.copy(
                text = state.password.value ?: "",
                isErrorVisible = passwordErrorVisible
            )

            // Статус
            selectStatus.state = selectStatus.state.copy(
                selectText = requireContext().getString(state.accountStatus.toMessageId())
            )

            // Роль
            selectRole.state = selectRole.state.copy(
                selectText = state.selectedRole?.let { requireContext().getString(it.toMessageId()) } ?: ""
            )

            // Школа — обновление списка и выбранного элемента
            schoolRegister.updateItems(state.availableSchools, emptyList())
            selectSchool.state = selectSchool.state.copy(
                selectText = state.selectedSchool?.name ?: ""
            )

            // Видимость блока школы
            val requiresSchool = state.selectedRole != Role.ADMIN && state.selectedRole != null
            schoolLayout.visibility = if (requiresSchool) View.VISIBLE else View.GONE

            btnSave.isEnabled = state.isFormValid() && !state.isLoading
        }
    }

    override fun updateEvent(event: UserEditEvent) {
        when (event) {
            UserEditEvent.NavigateBack -> navigationManager.navigate(NavCommand.Back)
        }
    }
}