package stud.euktop.schooljournal.presentation.auth.profile

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.Gender
import stud.euktop.schooljournal.databinding.FragmentRegBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check
import stud.euktop.schooljournal.presentation.common.utils.setup
import stud.euktop.schooljournal.presentation.common.utils.toMessageId
import stud.euktop.uikit.components.datePicker.SchJDatePicker
import stud.euktop.uikit.components.input.select.SchJSelect
import java.sql.Date
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentRegBinding, ProfileViewModel, ProfileState, Unit>() {
    override fun inflateBinding(
        i: LayoutInflater,
        c: ViewGroup?
    ) = FragmentRegBinding.inflate(i, c, false)

    override val viewModel: ProfileViewModel by viewModels()
    val focusTrack = FocusTrack()
    private var dialogRef: DatePickerDialog? = null
    private lateinit var register: SchJSelect.RegisterList<Gender>
    override fun setupUI() {
        binding.apply {
            firstNameInput.setup(focusTrack) { viewModel.firstNameSet(it) }
            lastNameInput.setup(focusTrack) { viewModel.lastNameSet(it) }
            surNameInput.setup(focusTrack) { viewModel.surNameSet(it) }
            phoneInput.setup(focusTrack) { viewModel.phoneSet(it) }
            emailInput.setup(focusTrack) { viewModel.emailSet(it) }
            register = genderSelect.RegisterList<Gender>(
                onCLick = { viewModel.genderSet(it) },
                toText = { ContextCompat.getString(requireContext(), it.toMessageId()) }).apply {
                items = Gender.entries
                register(childFragmentManager)
            }
            birthDateInput.setOnClickListener {
                val datePicker = SchJDatePicker(
                    context = requireContext(),
                    onDateSelected = { selectedDate ->
                        viewModel.birthDaySet(selectedDate)
                    }
                ).apply { state = state.copy(selectedDate = viewModel.state.value.birthDay) }
                if (dialogRef != null && dialogRef?.isShowing == true) {
                    dialogRef?.dismiss()
                }
                dialogRef = datePicker
                datePicker.showUnique()
            }
            focusTrack.onFocusChanged = { updateState() }
        }
    }

    @Inject
    internal lateinit var navigationManager: NavigationManager
    override fun updateState(state: ProfileState) {
        binding.apply {
            birthDateInput.state = birthDateInput.state.copy(
                text = if (state.birthDay == null) "" else state.dateFormat.format(
                    state.birthDay
                )
            )
            firstNameInput.check(focusTrack, state.firstName)
            lastNameInput.check(focusTrack, state.lastName)
            surNameInput.check(focusTrack, state.surName)
            phoneInput.check(focusTrack, state.phone)
            emailInput.check(focusTrack, state.email)
            genderSelect.state = genderSelect.state.copy(
                selectText = (if (state.gender == null) "" else register.toText(state.gender))
            )
            nextButton.isEnabled = state.isButtonActive()
        }
    }

    override fun updateEvent(event: Unit) {
        navigationManager.navigate(
            NavCommand.ToAction(ProfileFragmentDirections.actionProfileFragmentToCreatePasswordFragment())
        )
    }
}