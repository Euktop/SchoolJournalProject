package stud.euktop.schooljournal.presentation.main.admin.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.AccountStatus
import stud.euktop.domain.model.Role
import stud.euktop.domain.model.School
import stud.euktop.schooljournal.databinding.DialogUserEditBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.*
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
            // Основные поля
            inputLastName.setup(focusTrack) { viewModel.updateLastName(it) }
            inputFirstName.setup(focusTrack) { viewModel.updateFirstName(it) }
            inputSurName.setup(focusTrack) { viewModel.updateSurName(it) }
            inputEmail.setup(focusTrack) { viewModel.updateEmail(it) }
            inputPhone.setup(focusTrack) { viewModel.updatePhone(it) }
            inputPassword.setup(focusTrack) { viewModel.updatePassword(it) }

            // Статус
            selectStatus.RegisterList<AccountStatus>(
                onCLick = { viewModel.updateAccountStatus(it) },
                toText = { requireContext().getString(it.toMessageId()) }
            ).apply {
                items = AccountStatus.entries.toList()
                register(childFragmentManager)
            }

            // Роль (одиночный выбор)
            selectRole.RegisterList<Role>(
                onCLick = { role ->
                    viewModel.updateSelectedRole(role)
                    val requiresSchool = role != Role.ADMIN
                    tvSchoolLabel.visibility = if (requiresSchool) View.VISIBLE else View.GONE
                    selectSchool.visibility = if (requiresSchool) View.VISIBLE else View.GONE
                },
                toText = { requireContext().getString(it.toMessageId()) }
            ).apply {
                items = Role.entries.toList()
                register(childFragmentManager)
            }

            // Школа
            selectSchool.RegisterList<School>(
                onCLick = { school -> viewModel.updateSelectedSchool(school.schoolId) },
                toText = { it.name }
            ).apply {
                register(childFragmentManager)
            }

            btnSave.setOnClickListener { viewModel.save() }
            btnCancel.setOnClickListener { navigationManager.navigate(NavCommand.Back) }
        }
    }

    override fun updateState(state: UserEditState) {
        binding.apply {
            // Валидация ФИО, email, телефона
            inputChecks(
                focusTrack,
                inputLastName to state.lastName,
                inputFirstName to state.firstName,
                inputSurName to state.surName,
                inputEmail to state.email,
                inputPhone to state.phone
            )

            // Валидация пароля: при редактировании ошибка только если поле не пустое и невалидное
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
            val roleText =
                state.selectedRole?.let { requireContext().getString(it.toMessageId()) } ?: ""
            selectRole.state = selectRole.state.copy(selectText = roleText)

            // Школа – обновляем список и выбранное значение
            selectRole.adapter?.submitList(state.availableRoles)
            selectSchool.adapter?.submitList(state.availableSchools)
            val schoolText =
                state.availableSchools.find { it.schoolId == state.selectedSchoolId }?.name ?: ""
            selectSchool.state = selectSchool.state.copy(selectText = schoolText)

            // Видимость выбора школы
            val requiresSchool = state.selectedRole != Role.ADMIN && state.selectedRole != null
            tvSchoolLabel.visibility = if (requiresSchool) View.VISIBLE else View.GONE
            selectSchool.visibility = if (requiresSchool) View.VISIBLE else View.GONE

            // Кнопка сохранить доступна только при валидной форме и не во время загрузки
            btnSave.isEnabled = state.isFormValid() && !state.isLoading
        }
    }

    override fun updateEvent(event: UserEditEvent) {
        when (event) {
            UserEditEvent.NavigateBack -> navigationManager.navigate(NavCommand.Back)
        }
    }
}