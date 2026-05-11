package stud.euktop.schooljournal.presentation.common.binding

import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check
import stud.euktop.uikit.components.input.SchJInput

data class YearRange(
    val start: Int?,
    val end: Int?
)

fun bindYearRange(
    startInput: SchJInput,
    endInput: SchJInput,
    focusTrack: FocusTrack,
    onRangeChanged: (YearRange) -> Unit
) {
    startInput.bindIntField(focusTrack) { start ->
        onRangeChanged(YearRange(start, endInput.state.text.toIntOrNull()))
    }
    endInput.bindIntField(focusTrack) { end ->
        onRangeChanged(YearRange(startInput.state.text.toIntOrNull(), end))
    }
}

fun checkYearRange(
    startInput: SchJInput,
    endInput: SchJInput,
    focusTrack: FocusTrack,
    start: Int?,
    end: Int?
): Boolean {
    val startValid = start != null && start > 0
    val endValid = end != null && end > 0 && (start == null || end >= start)
    startInput.check(focusTrack, startValid)
    endInput.check(focusTrack, endValid)
    startInput.syncIntValue(start)
    endInput.syncIntValue(end)
    return startValid && endValid
}