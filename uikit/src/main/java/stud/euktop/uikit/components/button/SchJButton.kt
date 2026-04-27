package stud.euktop.uikit.components.button

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.withStyledAttributes
import stud.euktop.uikit.R
import stud.euktop.uikit.components.base.SchJBase
import stud.euktop.uikit.components.base.SchJState

class SchJButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = R.attr.schJButtonStyle
) : AppCompatButton(context, attrs, defStyleAttrs), SchJState<SchJButtonState> {
    private val schJBase = object : SchJBase<SchJButtonState>() {
        override fun initState() =
            SchJButtonState(SchJButtonState.ButtonType.BIG, SchJButtonState.ButtonClass.PRIMARY)

        override fun updateState(state: SchJButtonState) {
            state.updateView(this@SchJButton as TextView)
        }

        override fun setupUI() {
            updateState(state)
        }
    }
    override var state: SchJButtonState by schJBase

    override fun setEnabled(enabled: Boolean) {
        if (enabled && state.buttonClass == SchJButtonState.ButtonClass.INACTIVE)
            state = state.copy(buttonClass = SchJButtonState.ButtonClass.PRIMARY)
        else if (!enabled && state.buttonClass == SchJButtonState.ButtonClass.INACTIVE)
            state = state.copy(buttonClass = SchJButtonState.ButtonClass.INACTIVE)
        super.setEnabled(enabled)
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.SchJButton) {
            state = state.copy(
                buttonType = SchJButtonState.ButtonType.entries.firstOrNull {
                    it.ordinal == getInt(
                        R.styleable.SchJButton_schJButtonType,
                        SchJButtonState.ButtonType.BIG.ordinal
                    )
                } ?: SchJButtonState.ButtonType.BIG,
                buttonClass = SchJButtonState.ButtonClass.entries.firstOrNull {
                    it.ordinal == getInt(
                        R.styleable.SchJButton_schJButtonClass,
                        SchJButtonState.ButtonClass.PRIMARY.ordinal
                    )
                } ?: SchJButtonState.ButtonClass.PRIMARY,
            )
        }
    }
}