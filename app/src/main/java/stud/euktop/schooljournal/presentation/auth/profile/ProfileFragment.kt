package stud.euktop.schooljournal.presentation.auth.profile

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import stud.euktop.domain.model.Gender
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentRegBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.setup
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

class ProfileFragment : BaseFragment<FragmentRegBinding, ProfileViewModel, ProfileState, Unit>() {
    override fun inflateBinding(
        i: LayoutInflater,
        c: ViewGroup?
    ) = FragmentRegBinding.inflate(i, c, false)

    override val viewModel: ProfileViewModel by viewModels()
    val focusTrack = FocusTrack()

    override fun setupUI() {
        binding.apply {
            lastNameInput.setup(focusTrack) { viewModel.lastNameSet(it) }
            firstNameInput.setup(focusTrack) { viewModel.firstNameSet(it) }
            surNameInput.setup(focusTrack) { viewModel.surNameSet(it) }
            phoneInput.setup(focusTrack) { viewModel.phoneSet(it) }
            emailInput.setup(focusTrack) { viewModel.emailSet(it) }
            genderSelect.register(childFragmentManager, Gender.entries, {
                viewModel.genderSet(it)
            }, {
                ContextCompat.getString(
                    requireContext(), when (it) {
                        Gender.MALE -> R.string.male
                        Gender.WOMAN -> R.string.woman
                        Gender.NONE -> R.string.none
                    }
                )
            })
            birthDateInput.setOnClickListener {
                val calendar = Calendar.getInstance()
                val maxDate = calendar.timeInMillis
                calendar.add(Calendar.YEAR, -100)
                val minDate = calendar.timeInMillis

                DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        val selectedDate = GregorianCalendar(year, month, dayOfMonth).time
                        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        birthDateInput.state =
                            birthDateInput.state.copy(text = dateFormat.format(selectedDate))
                        viewModel.birthDaySet(selectedDate)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).apply {
                    datePicker.maxDate = maxDate
                    datePicker.minDate = minDate
                }.show()
            }
        }
    }

    override fun updateState(state: ProfileState) {

    }

    override fun updateEvent(event: Unit) {
    }
}