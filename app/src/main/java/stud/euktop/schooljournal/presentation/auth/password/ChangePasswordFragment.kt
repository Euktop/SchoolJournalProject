package stud.euktop.schooljournal.presentation.auth.password

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.utils.validation.ValidatorInterface
import stud.euktop.schooljournal.databinding.FragmentChangePasswordBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.binding.bindForm
import stud.euktop.schooljournal.presentation.common.binding.toInit
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<
        FragmentChangePasswordBinding,
        ChangePasswordViewModel,
        ChangePasswordState,
        Unit>() {

    override val viewModel: ChangePasswordViewModel by viewModels()
    private val focusTrack = FocusTrack()
    private lateinit var loadingDelegate: LoadingDelegate<ChangePasswordState>

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentChangePasswordBinding.inflate(inflater, container, false)

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        bindForm(focusTrack, viewModel) {
            field(binding.inputOldPassword, { it.oldPassword }, viewModel::updateOldPassword)
            field(binding.inputNewPassword, { it.newPassword }, viewModel::updateNewPassword)
            field(binding.inputConfirmPassword, {
                object : ValidatorInterface<String> {
                    override var value: String?
                        get() = it.confirmPassword
                        set(value) {
                            viewModel.updateConfirmPassword(value ?: "")
                        }

                    override fun validate(value: String?): Boolean =
                        it.newPassword.value == value

                    override fun getValidate(): String = it.confirmPassword
                }
            }, viewModel::updateConfirmPassword)
        }

        binding.buttonsSaveCancel.toInit(
            loadingDelegate,
            viewModel::changePassword,
            viewModel::cancel
        )
    }

    override fun updateState(state: ChangePasswordState) {
        binding.buttonsSaveCancel.btnSave.isEnabled = state.isFormValid()
    }

    override fun updateEvent(event: Unit) {}
}