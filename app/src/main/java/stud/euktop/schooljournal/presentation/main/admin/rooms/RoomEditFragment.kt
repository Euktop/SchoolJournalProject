package stud.euktop.schooljournal.presentation.main.admin.rooms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentRoomEditBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.binding.bindForm
import stud.euktop.schooljournal.presentation.common.binding.bindPagingSelect
import stud.euktop.schooljournal.presentation.common.binding.toInit
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.filter.school.SchoolFilterDialog
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check

@AndroidEntryPoint
class RoomEditFragment :
    BaseFragment<FragmentRoomEditBinding, RoomEditViewModel, RoomEditState, Unit>() {

    override val viewModel: RoomEditViewModel by viewModels()
    private val focusTrack = FocusTrack()
    private lateinit var loadingDelegate: LoadingDelegate<RoomEditState>
    private val schoolFilterFlow = MutableStateFlow(SchoolFilter())

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRoomEditBinding.inflate(inflater, container, false)

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        bindForm(focusTrack, viewModel) {
            field(binding.inputName, { it.name }, viewModel::updateName)
        }

        bindPagingSelect(
            select = binding.selectSchool,
            viewModel = viewModel,
            title = getString(R.string.school),
            filterFlow = schoolFilterFlow,
            getPagingDataFlow = viewModel::getSchoolsPagingDataFlow,
            getSelectedValue = { it.school },
            toText = { it?.name ?: "" },
            onSelected = viewModel::updateSchool,
            onFilterClick = { showSchoolFilterDialog() },
            fragmentManager = childFragmentManager,
            lifecycleOwner = viewLifecycleOwner
        )

        binding.buttonsSaveCancel.toInit(loadingDelegate, viewModel::save, viewModel::cancel)
    }

    private fun showSchoolFilterDialog() {
        SchoolFilterDialog(
            initialFilter = schoolFilterFlow.value,
            onFilterApplied = { schoolFilterFlow.value = it },
            onError = viewModel.onError
        ).show(childFragmentManager, "school_filter")
    }

    override fun updateState(state: RoomEditState) {
        binding.inputName.check(focusTrack, state.name.validate())
        binding.selectSchool.state = binding.selectSchool.state.copy(
            selectText = state.school?.name ?: ""
        )
        binding.buttonsSaveCancel.btnSave.isEnabled = state.isFormValid()
    }

    override fun updateEvent(event: Unit) {}
}