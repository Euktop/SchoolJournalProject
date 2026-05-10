package stud.euktop.schooljournal.presentation.common.binding

import androidx.fragment.app.Fragment
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.utils.observeState
import stud.euktop.uikit.components.datePicker.SchJDatePicker
import stud.euktop.uikit.components.input.SchJInput
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

inline fun <STATE : BaseState<STATE>> Fragment.bindDate(
    input: SchJInput,
    viewModel: BaseViewModel<STATE, *>,
    crossinline getter: (STATE) -> Date?,
    crossinline setter: (Date?) -> Unit,
    format: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
) {
    input.setOnClickListener {
        val datePicker = SchJDatePicker(requireContext()) { date ->
            setter(date)
            input.state = input.state.copy(text = format.format(date))
        }
        datePicker.state = datePicker.state.copy(selectedDate = getter(viewModel.state.value))
        datePicker.showUnique()
    }

    observeState(viewModel.state) { state ->
        val date = getter(state)
        input.state = input.state.copy(text = date?.let { format.format(it) } ?: "")
    }
}