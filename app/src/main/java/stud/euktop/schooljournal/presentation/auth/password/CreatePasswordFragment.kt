package stud.euktop.schooljournal.presentation.auth.password

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.Nav1Directions
import stud.euktop.schooljournal.databinding.FragmentCreatePasswordBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check
import stud.euktop.schooljournal.presentation.common.utils.setup
import stud.euktop.uikit.components.input.SchJInput
import javax.inject.Inject

@AndroidEntryPoint
class CreatePasswordFragment :
    BaseFragment<FragmentCreatePasswordBinding, CreatePasswordViewModel, CreatePasswordState, Unit>() {
    override fun inflateBinding(
        i: LayoutInflater, c: ViewGroup?
    ) = FragmentCreatePasswordBinding.inflate(i, c, false)

    override val viewModel: CreatePasswordViewModel by viewModels()
    private val focusTrack = FocusTrack()
    override fun setupUI() {
        binding.apply {
            MatuleInputNewPassword.setup(focusTrack) {
                viewModel.passwordValidatorSet(it)
            }
            MatuleInputRefreshPassword.listener =
                SchJInput.Listener { viewModel.nextPasswordNextValidatorSet(it) }
            MatuleButtonSave.setOnClickListener {
                viewModel.onSaveClick()
            }
        }
    }

    override fun updateState(state: CreatePasswordState) {
        binding.apply {
            MatuleButtonSave.isEnabled = state.isNextActive()
            MatuleInputNewPassword.apply {
                val changeValidate = this.state.isErrorVisible != state.passwordValidator.validate()
                binding.MatuleInputNewPassword.check(focusTrack, state.passwordValidator)
                if (changeValidate) focusTrack.removeFocus(MatuleInputRefreshPassword)
            }
            MatuleInputRefreshPassword.apply {
                if (state.passwordValidator.validate()) focusTrack.setFocusListener(this)
                this.state =
                    this.state.copy(
                        text = state.nextPasswordNextValidator,
                        isErrorVisible = focusTrack.isTouched(this) && state.nextPasswordNextValidator == state.passwordValidator.value
                    )
            }
        }
    }

    @Inject
    internal lateinit var navigationManager: NavigationManager
    override fun updateEvent(event: Unit) {
        navigationManager.navigate(NavCommand.ToAction(Nav1Directions.actionGlobalNavMainMain()))
    }
}