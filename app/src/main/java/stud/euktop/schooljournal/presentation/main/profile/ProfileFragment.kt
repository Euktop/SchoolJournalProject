package stud.euktop.schooljournal.presentation.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.utils.toBaseString
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentProfileBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam
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
        binding.btnLogout.setOnClickListener {
            messages.message(MessageParam(R.string.logout_message) {})
        }
        binding.btnChangePassword.setOnClickListener {
            router.toChangePassword()
        }
    }

    override fun updateState(state: ProfileState) {
        val user = state.user ?: return
        updateUI(user)
    }

    private fun updateUI(user: UserProfile) {
        binding.apply {
            tvUserName.text = "${user.lastName} ${user.firstName} ${user.surName ?: ""}".trim()
            tvEmail.text = user.email
            tvPhone.text = user.phone ?: getString(R.string.not_specified)
            tvBirthday.text = user.birthday?.toBaseString() ?: getString(R.string.not_specified)
            tvGender.text = user.gender.toMessageId().let { getString(it) }
            val initials =
                "${user.firstName.firstOrNull()}${user.lastName.firstOrNull()}".uppercase()
            tvInitials.text = initials

            rolesContainer.removeAllViews()
            user.roles.forEach { role ->
                val chip = Chip(requireContext()).apply {
                    text = when (role.role) {
                        Role.ADMIN -> getString(R.string.administrator)
                        else -> {
                            val schoolIdText =
                                role.schoolId?.let { "Школа $it" } ?: getString(R.string.no_school)
                            "${getString(role.role.toMessageId())} ($schoolIdText)"
                        }
                    }
                    isClickable = false
                    isCheckable = false
                }
                rolesContainer.addView(chip)
            }
        }
    }

    override fun updateEvent(event: Unit) {}
}