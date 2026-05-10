package stud.euktop.schooljournal.presentation.common.binding

import androidx.fragment.app.Fragment
import stud.euktop.domain.utils.validation.PasswordValidator
import stud.euktop.schooljournal.databinding.FragmentCreatePasswordBinding
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check
import stud.euktop.schooljournal.presentation.common.utils.observeState
import stud.euktop.schooljournal.presentation.common.utils.setup

interface CreatePasswordFormState {
    val password: PasswordValidator
    val confirmPassword: String
}

interface CreatePasswordFormActions {
    fun updatePassword(value: String)
    fun updateConfirmPassword(value: String)
}

fun <STATE, VM> FragmentCreatePasswordBinding.setupPasswordForm(
    fragment: Fragment,
    focusTrack: FocusTrack,
    viewModel: VM,
    action: CreatePasswordFormActions
) where
        STATE : BaseState<STATE>,
        STATE : CreatePasswordFormState,
        VM : BaseViewModel<STATE, *> {

    with(fragment) {
        bindForm(focusTrack, viewModel) {
            field(MatuleInputNewPassword, { it.password }, action::updatePassword)
            MatuleInputRefreshPassword.setup(focusTrack, action::updateConfirmPassword)
            observeState(viewModel.state) { state ->
                val passwordsMatch = state.password.value == state.confirmPassword
                MatuleInputRefreshPassword.check(focusTrack, passwordsMatch)
                if (!state.password.validate())
                    focusTrack.removeFocus(MatuleInputRefreshPassword)
            }
        }
    }
}