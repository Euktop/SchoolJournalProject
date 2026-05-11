package stud.euktop.schooljournal.presentation.auth.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.ActivityLoginBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.binding.bindForm
import stud.euktop.schooljournal.presentation.common.binding.bindLoading
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<ActivityLoginBinding, LoginViewModel, LoginState, Unit>() {

    @Inject
    lateinit var navigationManager: NavigationManager
    override val viewModel: LoginViewModel by viewModels()
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = ActivityLoginBinding.inflate(inflater, container, false)

    private val focusTrack = FocusTrack()
    private lateinit var loadingDelegate: LoadingDelegate<LoginState>

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)
        binding.matuleButtonAuth.bindLoading(
            loadingDelegate, "login",
            stud.euktop.schooljournal.R.string.logging_in
        )
        bindForm(focusTrack, viewModel) {
            field(binding.MatuleInputEmail, { it.email }, viewModel::emailSet)
            field(binding.MatuleInputPassword, { it.password }, viewModel::passwordSet)
        }
        binding.matuleButtonAuth.setOnClickListener { viewModel.onLoginClick() }
    }

    override fun updateState(state: LoginState) {
        binding.matuleButtonAuth.isEnabled = state.isButtonActive()
    }

    override fun updateEvent(event: Unit) {}
}