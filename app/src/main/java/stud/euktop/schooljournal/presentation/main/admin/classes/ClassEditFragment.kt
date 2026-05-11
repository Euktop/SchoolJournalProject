package stud.euktop.schooljournal.presentation.main.admin.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentClassEditBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.binding.bindForm
import stud.euktop.schooljournal.presentation.common.binding.bindIntField
import stud.euktop.schooljournal.presentation.common.binding.bindPagingSelect
import stud.euktop.schooljournal.presentation.common.binding.bindYearRange
import stud.euktop.schooljournal.presentation.common.binding.checkInt
import stud.euktop.schooljournal.presentation.common.binding.checkYearRange
import stud.euktop.schooljournal.presentation.common.binding.toInit
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.filter.school.SchoolFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.user.UserFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.user.toApp
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check

@AndroidEntryPoint
class ClassEditFragment :
    BaseFragment<FragmentClassEditBinding, ClassEditViewModel, ClassEditState, Unit>() {

    override val viewModel: ClassEditViewModel by viewModels()
    private val focusTrack = FocusTrack()
    private lateinit var loadingDelegate: LoadingDelegate<ClassEditState>

    private val schoolFilterFlow = MutableStateFlow(SchoolFilter())
    private val teacherFilterFlow = MutableStateFlow(UserFilter())

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentClassEditBinding.inflate(inflater, container, false)

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        bindForm(focusTrack, viewModel) {
            field(binding.inputLetter, { it.letter }, viewModel::updateLetter)
        }

        binding.inputGrade.bindIntField(focusTrack, viewModel::updateGrade)

        bindYearRange(
            startInput = binding.inputYearStart,
            endInput = binding.inputYearEnd,
            focusTrack = focusTrack
        ) { range ->
            viewModel.updateAcademicYearStart(range.start)
            viewModel.updateAcademicYearEnd(range.end)
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

        bindPagingSelect(
            select = binding.selectClassTeacher,
            viewModel = viewModel,
            title = getString(R.string.class_teacher),
            filterFlow = teacherFilterFlow,
            getPagingDataFlow = viewModel::getTeachersPagingDataFlow,
            getSelectedValue = { it.classTeacher },
            toText = { it?.let { "${it.lastName} ${it.firstName}" } ?: "" },
            onSelected = viewModel::updateClassTeacher,
            onFilterClick = { showTeacherFilterDialog() },
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

    private fun showTeacherFilterDialog() {
        UserFilterDialog(
            initialFilter = teacherFilterFlow.value.toApp(viewModel.state.value.school),
            onFilterApplied = { teacherFilterFlow.value = it.toDomainFilter() },
            onError = viewModel.onError
        ).show(childFragmentManager, "teacher_filter")
    }

    override fun updateState(state: ClassEditState) {
        binding.inputLetter.check(focusTrack, state.letter.validate())
        binding.inputGrade.checkInt(focusTrack, state.grade, state.grade in 1..11)

        val yearsValid = checkYearRange(
            binding.inputYearStart,
            binding.inputYearEnd,
            focusTrack,
            state.academicYearStart,
            state.academicYearEnd
        )
        val formValid = state.letter.validate() && (state.grade in 1..11) && yearsValid
        binding.selectSchool.state =
            binding.selectSchool.state.copy(selectText = state.school?.name ?: "")
        binding.selectClassTeacher.state =
            binding.selectClassTeacher.state.copy(selectText = state.classTeacher?.let { "${it.lastName} ${it.firstName}" }
                ?: "")

        binding.buttonsSaveCancel.btnSave.isEnabled = formValid
    }

    override fun updateEvent(event: Unit) {}
}