package stud.euktop.schooljournal.presentation.auth.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentRegBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.binding.bindLoading
import stud.euktop.schooljournal.presentation.common.binding.setupProfileForm
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentRegBinding, ProfileViewModel, ProfileState, Unit>() {

    override val viewModel: ProfileViewModel by viewModels()
    private val focusTrack = FocusTrack()
    private lateinit var loadingDelegate: LoadingDelegate<ProfileState>

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRegBinding.inflate(inflater, container, false)

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        binding.regItem.setupProfileForm(
            fragment = this,
            focusTrack = focusTrack,
            viewModel = viewModel
        )
        binding.nextButton.bindLoading(loadingDelegate, "save_profile", R.string.saving)
        binding.nextButton.setOnClickListener { viewModel.onNextClick() }
    }

    override fun updateState(state: ProfileState) {
        binding.nextButton.isEnabled = state.isButtonActive()
    }

    override fun updateEvent(event: Unit) {}
}