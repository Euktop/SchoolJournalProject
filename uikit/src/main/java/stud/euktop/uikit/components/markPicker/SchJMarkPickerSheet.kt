package stud.euktop.uikit.components.markPicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import stud.euktop.uikit.components.bottomsheet.SchJBottomSheet

class SchJMarkPickerSheet(
    private val state: SchJMarkPickerState,
    private val listener: SchJMarkPicker.Listener
) : SchJBottomSheet() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val picker = SchJMarkPicker(requireContext())
        picker.state = state
        picker.listener = { value ->
            listener.setOnClick(value)
            dismiss()
        }
        return picker
    }
}