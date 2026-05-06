package stud.euktop.schooljournal.presentation.main.admin.lessons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.utils.toBaseString
import stud.euktop.schooljournal.databinding.FragmentLessonEditBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.DomainSelectHelper.setupSearchableSelect
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check
import stud.euktop.schooljournal.presentation.common.utils.setup
import stud.euktop.uikit.components.datePicker.SchJDatePicker
import stud.euktop.uikit.components.input.select.searchable.SchJSearchableSelect
import javax.inject.Inject

@AndroidEntryPoint
class LessonEditFragment : BaseFragment<
        FragmentLessonEditBinding,
        LessonEditViewModel,
        LessonEditState,
        LessonEditEvent>() {

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentLessonEditBinding.inflate(i, c, false)

    override val viewModel: LessonEditViewModel by viewModels()

    private val focusTrack = FocusTrack()

    @Inject
    lateinit var navigationManager: NavigationManager

    private lateinit var classRegister: SchJSearchableSelect.RegisterList<ClassInfo>
    private lateinit var subjectRegister: SchJSearchableSelect.RegisterList<Subject>
    private lateinit var teacherRegister: SchJSearchableSelect.RegisterList<UserInfo>
    private lateinit var roomRegister: SchJSearchableSelect.RegisterList<Room>

    override fun setupUI() {
        binding.apply {
            inputTopic.setup(focusTrack) { viewModel.updateTopic(it) }
            inputStartTime.setup(focusTrack) { viewModel.updateStartTime(it) }
            inputEndTime.setup(focusTrack) { viewModel.updateEndTime(it) }
            inputLocation.setup(focusTrack) { viewModel.updateLocationAddress(it) }

            // Используем DomainSelectHelper для настройки селектов
            classRegister = lifecycleScope.setupSearchableSelect(
                select = selectClass,
                fragmentManager = childFragmentManager,
                loadItems = { viewModel.loadClasses() },
                toText = { it?.name ?: "" },
                onSelected = { classInfo -> viewModel.updateClass(classInfo) }
            )

            subjectRegister = lifecycleScope.setupSearchableSelect(
                select = selectSubject,
                fragmentManager = childFragmentManager,
                loadItems = { viewModel.loadSubjects() },
                toText = { it?.name ?: "" },
                onSelected = { subject -> viewModel.updateSubject(subject) }
            )

            teacherRegister = lifecycleScope.setupSearchableSelect(
                select = selectTeacher,
                fragmentManager = childFragmentManager,
                loadItems = { viewModel.loadTeachers() },
                toText = { it?.firstName ?: "" },
                onSelected = { teacher -> viewModel.updateTeacher(teacher) }
            )

            roomRegister = lifecycleScope.setupSearchableSelect(
                select = selectRoom,
                fragmentManager = childFragmentManager,
                loadItems = { viewModel.loadRooms() },
                toText = { it?.name ?: "" },
                onSelected = { room -> viewModel.updateRoom(room) }
            )

            // Дата
            inputDate.setOnClickListener {
                val datePicker = SchJDatePicker(requireContext()) { date ->
                    viewModel.updateDate(date)
                    inputDate.state = inputDate.state.copy(text = date.toBaseString())
                }
                datePicker.state = datePicker.state.copy(selectedDate = viewModel.state.value.date)
                datePicker.showUnique()
            }

            saveCancel.btnSave.setOnClickListener { viewModel.save() }
            saveCancel.btnCancel.setOnClickListener { navigationManager.navigate(NavCommand.Back) }
        }
    }

    override fun updateState(state: LessonEditState) {
        binding.apply {
            inputTopic.check(focusTrack, state.topic.isNotBlank())
            inputStartTime.check(focusTrack, state.startTime.isNotBlank())
            inputEndTime.check(focusTrack, state.endTime.isNotBlank())

            classRegister.updateItems(state.availableClasses)
            subjectRegister.updateItems(state.availableSubjects)
            teacherRegister.updateItems(state.availableTeachers)
            roomRegister.updateItems(state.availableRooms)

            selectClass.state = selectClass.state.copy(selectText = state.selectedClass?.name ?: "")
            selectSubject.state =
                selectSubject.state.copy(selectText = state.selectedSubject?.name ?: "")
            selectTeacher.state =
                selectTeacher.state.copy(selectText = state.selectedTeacher?.let { "${it.lastName} ${it.firstName}" }
                    ?: "")
            selectRoom.state = selectRoom.state.copy(selectText = state.selectedRoom?.name ?: "")

            inputDate.state = inputDate.state.copy(text = state.date?.toBaseString() ?: "")

            saveCancel.btnSave.isEnabled = state.isFormValid() && !state.isLoading
        }
    }

    override fun updateEvent(event: LessonEditEvent) {
        when (event) {
            LessonEditEvent.NavigateBack -> navigationManager.navigate(NavCommand.Back)
        }
    }
}