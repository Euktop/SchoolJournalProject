package stud.euktop.schooljournal.presentation.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.utils.toBaseString
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentProfileBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterProfile
import stud.euktop.schooljournal.presentation.common.utils.toMessageId
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<
        FragmentProfileBinding,
        ProfileViewModel,
        ProfileState,
        Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)

    override val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var router: RouterProfile

    override fun setupUI() {
        binding.btnEditProfile.setOnClickListener {
            val currentUser = viewModel.state.value.user ?: return@setOnClickListener
            router.toEditUser(currentUser.userId)
        }

        binding.btnChangePassword.setOnClickListener {
            router.toChangePassword()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        binding.btnEditAvatar.setOnClickListener {
            // TODO: Открыть диалог выбора фото или перехода в редактирование аватара
        }
    }

    override fun updateState(state: ProfileState) {
        val user = state.user ?: return
        updateUI(user)
    }

    private fun updateUI(user: UserProfile) {
        binding.apply {
            // Имя и инициалы
            tvUserName.text = "${user.lastName} ${user.firstName}".trim()
            val initials = "${user.firstName.firstOrNull()}${user.lastName.firstOrNull()}".uppercase()
            tvInitials.text = initials

            // Роль (берем первую роль из списка)
            val role = user.roles.firstOrNull()?.role
            tvRole.text = role?.let { getString(it.toMessageId()) } ?: getString(R.string.not_specified)

            // Контакты
            tvEmail.text = user.email
            tvPhone.text = user.phone ?: getString(R.string.not_specified)

            // Школа (пока заглушка, так как в UserProfile нет названия школы)
            // TODO: Добавить загрузку названия школы через SchoolAdminRepository
            tvSchool.text = getString(R.string.not_specified)

            // Дата рождения
            tvBirthday.text = user.birthday?.toBaseString() ?: getString(R.string.not_specified)
        }
    }

    override fun updateEvent(event: Unit) {}
}