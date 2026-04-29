package stud.euktop.schooljournal.presentation.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentProfileBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam

//stud.euktop.schooljournal.presentation.main.profile.ProfileFragment
@AndroidEntryPoint
class ProfileFragment : BaseFragment<
        FragmentProfileBinding,
        ProfileViewModel,
        ProfileState,
        Unit
        >() {
    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentProfileBinding.inflate(i, c, false)

    override val viewModel: ProfileViewModel by viewModels()

    override fun setupUI() {
        binding.btnLogout.setOnClickListener {
            messages.message(MessageParam(R.string.logout_message) {})
        }
    }

    override fun updateState(state: ProfileState) {
        binding.tvUserName.text = state.userName
        binding.tvEmail.text = state.email

        val initials =
            state.userName.split(" ").take(2).map { it.firstOrNull() ?: ' ' }.joinToString("")
        binding.tvInitials.text = initials.uppercase()

        binding.rolesContainer.removeAllViews()
        state.roleNames.forEach { role ->
            val chip = Chip(context).apply {
                text = role
                isClickable = false
                isCheckable = false
            }
            binding.rolesContainer.addView(chip)
        }
    }

    override fun updateEvent(event: Unit) {}
}