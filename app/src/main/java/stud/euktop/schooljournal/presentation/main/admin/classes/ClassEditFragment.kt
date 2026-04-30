// presentation/main/admin/classes/ClassEditFragment.kt
package stud.euktop.schooljournal.presentation.main.admin.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.School
import stud.euktop.domain.model.UserInfo
import stud.euktop.schooljournal.databinding.FragmentClassEditBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check
import stud.euktop.schooljournal.presentation.common.utils.inputChecks
import stud.euktop.schooljournal.presentation.common.utils.setup
import stud.euktop.schooljournal.presentation.common.utils.submitList
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

    override fun setupUI() {
        binding.apply {
            // Поля ввода
            inputGrade.setup(focusTrack) { value ->
                viewModel.updateGrade(value.toIntOrNull())
            }
            inputLetter.setup(focusTrack) { viewModel.updateLetter(it) }
            inputYearStart.setup(focusTrack) { value ->
                viewModel.updateAcademicYearStart(value.toIntOrNull())
            }
            inputYearEnd.setup(focusTrack) { value ->
                viewModel.updateAcademicYearEnd(value.toIntOrNull())
            }

            // Выбор школы
            selectSchool.RegisterList<School>(
                onCLick = { school ->
                    viewModel.updateSchool(school.schoolId, school.name)
                },
                toText = { it.name }
            ).apply {
                register(childFragmentManager)
            }

            // Выбор классного руководителя
            selectClassTeacher.RegisterList<UserInfo>(
                onCLick = { teacher ->
                    viewModel.updateClassTeacher(
                        teacher.userId,
                        "${teacher.lastName} ${teacher.firstName}"
                    )
                },
                toText = { "${it.lastName} ${it.firstName}" }
            ).apply {
                register(childFragmentManager)
            }

            // Кнопки (из include)
            val buttons = binding.buttonsSaveCancel
            buttons.btnSave.setOnClickListener { viewModel.save() }
            buttons.btnCancel.setOnClickListener { navigationManager.navigate(NavCommand.Back) }
        }
    }

    override fun updateState(state: ClassEditState) {
        binding.apply {
            inputChecks(
                focusTrack,
                inputLetter to state.letter
            )
            val gradeValid = state.grade != null && state.grade in 1..11
            inputGrade.check(focusTrack, gradeValid)

            val start = state.academicYearStart
            val end = state.academicYearEnd
            inputYearStart.check(focusTrack, start != null && start > 0)
            inputYearEnd.check(
                focusTrack,
                end != null && end > 0 && (start == null || end >= start)
            )

            selectSchool.adapter?.submitList(state.availableSchools)
            selectClassTeacher.adapter?.submitList(state.availableTeachers)

            selectSchool.state = selectSchool.state.copy(
                selectText = state.selectedSchoolName
            )
            selectClassTeacher.state = selectClassTeacher.state.copy(
                selectText = state.selectedTeacherName
            )

            val buttons = binding.buttonsSaveCancel
            buttons.btnSave.isEnabled = state.isFormValid()
        }
    }

    override fun updateEvent(event: ClassEditEvent) {
        when (event) {
            ClassEditEvent.NavigateBack -> navigationManager.navigate(NavCommand.Back)
        }
    }
}