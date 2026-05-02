package stud.euktop.schooljournal.presentation.main.admin.assignments

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.schooljournal.databinding.FragmentTeacherAssignmentEditBinding
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.main.admin.common.base.BaseEditFragment
import stud.euktop.uikit.components.datePicker.SchJDatePicker
import stud.euktop.uikit.components.input.select.ListSafe
import stud.euktop.uikit.components.input.select.searchable.SchJSearchableSelect
import javax.inject.Inject

@AndroidEntryPoint
class TeacherAssignmentEditFragment :
    BaseEditFragment<
            FragmentTeacherAssignmentEditBinding,
            TeacherAssignmentEditViewModel,
            TeacherAssignmentEditState
            >() {

    @Inject
    override lateinit var navigationManager: NavigationManager

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentTeacherAssignmentEditBinding.inflate(i, c, false)

    override val viewModel: TeacherAssignmentEditViewModel by viewModels()

    private val focusTrack = FocusTrack()
    private lateinit var teacherRegister: SchJSearchableSelect.RegisterList<UserInfo>
    private lateinit var classRegister: SchJSearchableSelect.RegisterList<ClassInfo>
    private lateinit var subjectRegister: SchJSearchableSelect.RegisterList<Subject>
    private var fromDateDialog: DatePickerDialog? = null
    private var toDateDialog: DatePickerDialog? = null

    override fun setupUI() {
        binding.apply {
            // Учитель
            val teacherListSafe = ListSafe<UserInfo>(
                toText = { it?.let { "${it.lastName} ${it.firstName}" } ?: "" },
                onClick = { teacher, _ -> viewModel.updateTeacher(teacher) }
            )
            teacherRegister = selectTeacher.RegisterList(teacherListSafe)
            teacherRegister.register(childFragmentManager)
            selectTeacher.onShowing = { viewModel.loadTeachers("") }

            // Класс
            val classListSafe = ListSafe<ClassInfo>(
                toText = { it?.name ?: "" },
                onClick = { classInfo, _ -> viewModel.updateClass(classInfo) }
            )
            classRegister = selectClass.RegisterList(classListSafe)
            classRegister.register(childFragmentManager)
            selectClass.onShowing = { viewModel.loadClasses("") }

            // Предмет
            val subjectListSafe = ListSafe<Subject>(
                toText = { it?.name ?: "" },
                onClick = { subject, _ -> viewModel.updateSubject(subject) }
            )
            subjectRegister = selectSubject.RegisterList(subjectListSafe)
            subjectRegister.register(childFragmentManager)
            selectSubject.onShowing = { viewModel.loadSubjects("") }

            inputValidFrom.setOnClickListener {
                showDatePicker(true)
            }
            inputValidTo.setOnClickListener {
                showDatePicker(false)
            }

            chkIsPrimary.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateIsPrimary(isChecked)
            }

            buttonsSaveCancel.btnSave.setOnClickListener { viewModel.save() }
            buttonsSaveCancel.btnCancel.setOnClickListener { navigationManager.navigate(NavCommand.Back) }
        }
    }

    private fun showDatePicker(isFrom: Boolean) {
        val currentDate =
            if (isFrom) viewModel.state.value.validFrom else viewModel.state.value.validTo
        val datePicker = SchJDatePicker(
            context = requireContext(),
            onDateSelected = { selectedDate ->
                if (isFrom) viewModel.updateValidFrom(selectedDate)
                else viewModel.updateValidTo(selectedDate)
            }
        ).apply {
            state = state.copy(selectedDate = currentDate)
        }
        if (isFrom) fromDateDialog?.dismiss()
        else toDateDialog?.dismiss()
        if (isFrom) fromDateDialog = datePicker
        else toDateDialog = datePicker
        datePicker.showUnique()
    }

    override fun updateState(state: TeacherAssignmentEditState) {
        binding.apply {
            teacherRegister.updateItems(state.availableTeachers)
            classRegister.updateItems(state.availableClasses)
            subjectRegister.updateItems(state.availableSubjects)

            selectTeacher.state = selectTeacher.state.copy(
                selectText = state.teacher?.let { "${it.lastName} ${it.firstName}" } ?: ""
            )
            selectClass.state = selectClass.state.copy(
                selectText = state.classInfo?.let { "${it.grade}${it.letter} (${it.school.name})" }
                    ?: ""
            )
            selectSubject.state = selectSubject.state.copy(
                selectText = state.subject?.name ?: ""
            )

            inputValidFrom.state = inputValidFrom.state.copy(
                text = state.validFrom?.let { state.dateFormat.format(it) } ?: ""
            )
            inputValidTo.state = inputValidTo.state.copy(
                text = state.validTo?.let { state.dateFormat.format(it) } ?: ""
            )
            chkIsPrimary.isChecked = state.isPrimary

            buttonsSaveCancel.btnSave.isEnabled = state.isFormValid() && !state.isLoading
        }
    }
}