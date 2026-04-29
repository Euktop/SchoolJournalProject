package stud.euktop.uikit.components.markPicker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SchJMarkPickerState(
    val studentName: String = "",
    val absenceTypes: AbsenceTypes? = null
) : Parcelable