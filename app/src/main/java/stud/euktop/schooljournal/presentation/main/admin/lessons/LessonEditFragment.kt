package stud.euktop.schooljournal.presentation.main.admin.lessons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.domain.utils.toBaseString
import stud.euktop.schooljournal.databinding.FragmentLessonEditBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.utils.DomainSelectHelper.setupSearchableSelect
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check
import stud.euktop.schooljournal.presentation.common.utils.setup
import stud.euktop.uikit.components.datePicker.SchJDatePicker

@AndroidEntryPoint
class LessonEditFragment : BaseFragment<
        FragmentLessonEditBinding,
        LessonEditViewModel,
        LessonEditState,
        Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLessonEditBinding.inflate(inflater, container, false)

    override val viewModel: LessonEditViewModel by viewModels()

    private val focusTrack = FocusTrack()

    override fun setupUI() {
        binding.apply {
            inputTopic.setup(focusTrack, viewModel::updateTopic)
            inputStartTime.setup(focusTrack, viewModel::updateStartTime)
            inputEndTime.setup(focusTrack, viewModel::updateEndTime)
            inputLocation.setup(focusTrack, viewModel::updateLocationAddress)

            lifecycleScope.launch {
                setupSearchableSelect(
                    select = selectClass,
                    fragmentManager = childFragmentManager,
                    loadItems = { viewModel.loadClasses() },
                    toText = { it?.let { "${it.grade}${it.letter}" } ?: "" },
                    onSelected = viewModel::updateClass,
                    showFilterDialog = null,
                    initialSelectedItem = null
                )

                // Предмет
                setupSearchableSelect(
                    select = selectSubject,
                    fragmentManager = childFragmentManager,
                    loadItems = { viewModel.loadSubjects() },
                    toText = { it?.name ?: "" },
                    onSelected = viewModel::updateSubject,
                    showFilterDialog = null,
                    initialSelectedItem = null
                )

                // Учитель
                setupSearchableSelect<UserListItem>(
                    select = selectTeacher,
                    fragmentManager = childFragmentManager,
                    loadItems = { viewModel.loadTeachers() },
                    toText = { it?.let { "${it.lastName} ${it.firstName}" } ?: "" },
                    onSelected = viewModel::updateTeacher,
                    showFilterDialog = null,
                    initialSelectedItem = null
                )

                // Кабинет
                setupSearchableSelect(
                    select = selectRoom,
                    fragmentManager = childFragmentManager,
                    loadItems = { viewModel.loadRooms() },
                    toText = { it?.name ?: "" },
                    onSelected = viewModel::updateRoom,
                    showFilterDialog = null,
                    initialSelectedItem = null
                )
            }

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
            saveCancel.btnCancel.setOnClickListener { viewModel.cancel() }
        }
    }

    override fun updateState(state: LessonEditState) {
        binding.apply {
            inputTopic.check(focusTrack, state.topic.isNotBlank())
            inputStartTime.check(focusTrack, state.startTime.isNotBlank())
            inputEndTime.check(focusTrack, state.endTime.isNotBlank())

            selectClass.state =
                selectClass.state.copy(selectText = state.selectedClass?.let { "${it.grade}${it.letter}" }
                    ?: "")
            selectSubject.state =
                selectSubject.state.copy(selectText = state.selectedSubject?.name ?: "")
            selectTeacher.state =
                selectTeacher.state.copy(selectText = state.selectedTeacher?.let { "${it.lastName} ${it.firstName}" }
                    ?: "")
            selectRoom.state = selectRoom.state.copy(selectText = state.selectedRoom?.name ?: "")

            inputDate.state = inputDate.state.copy(text = state.date?.toBaseString() ?: "")

            saveCancel.btnSave.isEnabled = state.isFormValid() && !state.isAnyLoading()
        }
    }

    override fun updateEvent(event: Unit) {}
}