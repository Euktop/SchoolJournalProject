package stud.euktop.schooljournal.presentation.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import stud.euktop.domain.utils.toBaseString
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentProfileBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.utils.toMessageId

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileViewModel, ProfileState, Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)

    override val viewModel: ProfileViewModel by viewModels()

    override fun setupUI() {
        binding.btnEditProfile.setOnClickListener {
            viewModel.buttonProfileClick()
        }

        binding.btnChangePassword.setOnClickListener {
            viewModel.changePasswordClick()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        /*        // Кнопка аватара пока не используется – скрываем
                binding.btnEditAvatar.visibility = View.GONE*/

        // TODO: реализовать выбор и загрузку аватара через ActivityResultContracts.GetContent
        binding.btnEditAvatar.setOnClickListener {
            logger?.d(this.toSimpleTag(), "Avatar clicked", "TODO: implement avatar selection/upload")
        }

        // Редактирование роли – открываем SelectRoleFragment
        binding.btnEditRole.setOnClickListener {
            viewModel.onRoleClick()
        }
    }

    override fun updateState(state: ProfileState) {
        try {
            logger?.d(this.toSimpleTag(), "updateState", "user=${state.user?.email}, role=${state.role}")
        } catch (_: Throwable) {
        }
        state.apply {
            updateUI(user, role, school)
        }
    }

    private fun updateUI(user: UserProfile?, role: Role?, school: School?) {
        binding.apply {
            tvUserName.text = user?.let { "${it.lastName} ${it.firstName}".trim() }
                ?: getString(R.string.not_specified)
            tvInitials.text =
                user?.let { "${it.firstName.firstOrNull()}${it.lastName.firstOrNull()}".uppercase() }
                    ?: getString(R.string.not_specified)

            tvRole.text =
                role?.let { getString(it.toMessageId()) } ?: getString(R.string.not_specified)

            tvEmail.text = user?.email ?: getString(R.string.not_specified)
            tvPhone.text = user?.phone ?: getString(R.string.not_specified)

            tvSchool.text = school?.name ?: getString(R.string.not_specified)

            tvBirthday.text = user?.birthday?.toBaseString() ?: getString(R.string.not_specified)
        }
    }

    override fun updateEvent(event: Unit) {}
}