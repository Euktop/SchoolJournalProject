package stud.euktop.schooljournal.presentation.main.admin.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.schooljournal.databinding.FragmentClassEditBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.*
import stud.euktop.uikit.components.input.select.ListSafe
import stud.euktop.uikit.components.input.select.searchable.SchJSearchableSelect
import javax.inject.Inject

@AndroidEntryPoint
class ClassEditFragment : BaseFragment<
        FragmentClassEditBinding,
        ClassEditViewModel,
        ClassEditState,
        ClassEditEvent
        >() {

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentClassEditBinding.inflate(i, c, false)

    override val viewModel: ClassEditViewModel by viewModels()

    private val focusTrack = FocusTrack()

    @Inject
    lateinit var navigationManager: NavigationManager

    private lateinit var schoolRegister: SchJSearchableSelect.RegisterList<School>
    private lateinit var teacherRegister: SchJSearchableSelect.RegisterList<UserInfo>

    override fun setupUI() {
        binding.apply {
            inputGrade.setup(focusTrack) { value -> viewModel.updateGrade(value.toIntOrNull()) }
            inputLetter.setup(focusTrack) { viewModel.updateLetter(it) }
            inputYearStart.setup(focusTrack) { value -> viewModel.updateAcademicYearStart(value.toIntOrNull()) }
            inputYearEnd.setup(focusTrack) { value -> viewModel.updateAcademicYearEnd(value.toIntOrNull()) }

            val schoolListSafe = ListSafe<School>(
                toText = { it?.name ?: "" },
                onClick = { school, _ -> viewModel.updateSchool(school) }
            )
            schoolRegister = selectSchool.RegisterList(schoolListSafe)
            schoolRegister.register(childFragmentManager)
            selectSchool.onShowing = { viewModel.loadSchools() }

            val teacherListSafe = ListSafe<UserInfo>(
                toText = { it?.fullName ?: "" },
                onClick = { teacher, _ -> viewModel.updateClassTeacher(teacher) }
            )
            teacherRegister = selectClassTeacher.RegisterList(teacherListSafe)
            teacherRegister.register(childFragmentManager)

            buttonsSaveCancel.btnSave.setOnClickListener { viewModel.save() }
            buttonsSaveCancel.btnCancel.setOnClickListener { navigationManager.navigate(NavCommand.Back) }
        }
    }

    override fun updateState(state: ClassEditState) {
        binding.apply {
            inputChecks(focusTrack, inputLetter to state.letter)
            val gradeValid = state.grade != null && state.grade in 1..11
            inputGrade.check(focusTrack, gradeValid)
            val start = state.academicYearStart
            val end = state.academicYearEnd
            inputYearStart.check(focusTrack, start != null && start > 0)
            inputYearEnd.check(
                focusTrack,
                end != null && end > 0 && (start == null || end >= start)
            )

            schoolRegister.updateItems(state.availableSchools)
            teacherRegister.updateItems(state.availableTeachers)

            selectSchool.state = selectSchool.state.copy(
                selectText = state.selectedSchool?.name ?: ""
            )
            selectClassTeacher.state = selectClassTeacher.state.copy(
                selectText = state.selectedTeacher?.let { "${it.lastName} ${it.firstName}" } ?: ""
            )

            buttonsSaveCancel.btnSave.isEnabled = state.isFormValid() && !state.isLoading
        }
    }

    override fun updateEvent(event: ClassEditEvent) {
        when (event) {
            ClassEditEvent.NavigateBack -> navigationManager.navigate(NavCommand.Back)
        }
    }
}