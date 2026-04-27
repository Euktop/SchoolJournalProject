package stud.euktop.schooljournal.presentation.auth.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import stud.euktop.schooljournal.databinding.ActivityLoginBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check
import stud.euktop.schooljournal.presentation.common.utils.setup

class LoginFragment : BaseFragment<ActivityLoginBinding, LoginViewModel, LoginState, Unit>() {
    override fun inflateBinding(
        i: LayoutInflater,
        c: ViewGroup?
    ) = ActivityLoginBinding.inflate(i, c, false)

    override val viewModel: LoginViewModel by viewModels()
    val focusTrack = FocusTrack()

    override fun setupUI() {
        binding.apply {
            MatuleInputEmail.setup(focusTrack) { viewModel.emailSet(it) }
            MatuleInputPassword.setup(focusTrack) { viewModel.passwordSet(it) }
            matuleButtonAuth.isEnabled = false
            matuleButtonAuth.setOnClickListener {

            }
        }
    }

    override fun updateState(state: LoginState) {
        binding.MatuleInputEmail.check(focusTrack, state.emailValidator)
        binding.MatuleInputPassword.check(focusTrack, state.passwordValidator)
        binding.matuleButtonAuth.isEnabled = state.isButtonActive()
    }

    override fun updateEvent(event: Unit) {
    }
}