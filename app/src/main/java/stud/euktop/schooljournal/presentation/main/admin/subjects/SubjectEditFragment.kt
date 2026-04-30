// presentation/main/admin/subjects/SubjectEditFragment.kt
package stud.euktop.schooljournal.presentation.main.admin.subjects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentSubjectEditBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check
import stud.euktop.schooljournal.presentation.common.utils.setup
import javax.inject.Inject

@AndroidEntryPoint
class SubjectEditFragment : BaseFragment<
        FragmentSubjectEditBinding,
        SubjectEditViewModel,
        SubjectEditState,
        SubjectEditEvent
        >() {

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentSubjectEditBinding.inflate(i, c, false)

    override val viewModel: SubjectEditViewModel by viewModels()

    private val focusTrack = FocusTrack()

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun setupUI() {
        binding.apply {
            inputName.setup(focusTrack) { viewModel.updateName(it) }
            inputDescription.setup(focusTrack) { viewModel.updateDescription(it) }
            buttonsSaveCancel.btnSave.setOnClickListener { viewModel.save() }
            buttonsSaveCancel.btnCancel.setOnClickListener { navigationManager.navigate(NavCommand.Back) }
        }
    }

    override fun updateState(state: SubjectEditState) {
        binding.apply {
            inputName.check(focusTrack, state.name)
            buttonsSaveCancel.btnSave.isEnabled = state.isFormValid()
        }
    }

    override fun updateEvent(event: SubjectEditEvent) {
        when (event) {
            SubjectEditEvent.NavigateBack -> navigationManager.navigate(NavCommand.Back)
        }
    }
}