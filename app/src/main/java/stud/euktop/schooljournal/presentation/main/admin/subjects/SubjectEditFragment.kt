package stud.euktop.schooljournal.presentation.main.admin.subjects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentSubjectEditBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.binding.bindForm
import stud.euktop.schooljournal.presentation.common.binding.toInit
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.domain.utils.loger.logger

@AndroidEntryPoint
class SubjectEditFragment : BaseFragment<
        FragmentSubjectEditBinding,
        SubjectEditViewModel,
        SubjectEditState,
        Unit>(), ToolbarConfigProvider {

    override val viewModel: SubjectEditViewModel by viewModels()
    private val focusTrack = FocusTrack()
    private lateinit var loadingDelegate: LoadingDelegate<SubjectEditState>

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSubjectEditBinding.inflate(inflater, container, false)

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        bindForm(focusTrack, viewModel) {
            field(binding.inputName, { it.name }, viewModel::updateName)
            field(binding.inputDescription, { it.description }, viewModel::updateDescription)
        }

        binding.buttonsSaveCancel.toInit(loadingDelegate, viewModel::save, viewModel::cancel)
    }

     override fun updateState(state: SubjectEditState) {
         logger?.d(this::class.java.simpleName, "updateState", "name: ${state.name}, form valid: ${state.isFormValid()}")
         binding.buttonsSaveCancel.btnSave.isEnabled = state.isFormValid()
     }

    override fun updateEvent(event: Unit) {}

    override fun getToolbarConfig() = ToolbarConfig(
        titleRes = R.string.edit_subject
    )
}