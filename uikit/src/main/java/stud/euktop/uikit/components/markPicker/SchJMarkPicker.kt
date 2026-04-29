package stud.euktop.uikit.components.markPicker

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import stud.euktop.uikit.components.base.SchJBaseBinding
import stud.euktop.uikit.components.base.SchJState
import stud.euktop.uikit.components.button.SchJButton
import stud.euktop.uikit.components.button.SchJButtonState
import stud.euktop.uikit.databinding.LayoutMarkPickerBinding

class SchJMarkPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), SchJState<SchJMarkPickerState> {
    var listener: Listener? = null

    private val base = object : SchJBaseBinding<LayoutMarkPickerBinding, SchJMarkPickerState>() {
        override fun initBinding() = LayoutMarkPickerBinding.inflate(
            android.view.LayoutInflater.from(context), this@SchJMarkPicker, true
        )

        override fun initState() = SchJMarkPickerState()
        override fun updateState(state: SchJMarkPickerState) {
            binding.tvStudentName.text = state.studentName
            buttons?.forEach { (types, button) ->
                button.state = if (types == state.absenceTypes)
                    button.state.copy(buttonClass = SchJButtonState.ButtonClass.SELECT)
                else button.state.copy(buttonClass = SchJButtonState.ButtonClass.UNSELECT)
            }
        }

        private var buttons: MutableMap<AbsenceTypes, SchJButton>? = null
        override fun setupUI() {
            binding.apply {
                buttons = mutableMapOf(
                    AbsenceTypes.G2 to btnMark2,
                    AbsenceTypes.G3 to btnMark3,
                    AbsenceTypes.G4 to btnMark4,
                    AbsenceTypes.G5 to btnMark5,
                    AbsenceTypes.IRRESPECTABLE to btnIrrespectable,
                    AbsenceTypes.ILL to btnIll,
                    AbsenceTypes.RESPECTABLE to btnRespectable
                )
                buttons?.forEach { (types, button) ->
                    button.state =
                        button.state.copy(buttonClass = SchJButtonState.ButtonClass.UNSELECT)
                    button.setOnClickListener {
                        listener?.setOnClick(types)
                    }
                }
            }
        }
    }

    override var state: SchJMarkPickerState by base

    fun interface Listener {
        fun setOnClick(absenceType: AbsenceTypes)
    }
}